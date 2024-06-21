package com.koh.rsa.handler;

import com.koh.rsa.annotation.RsaSecure;
import com.koh.rsa.enums.RsaMode;
import com.koh.rsa.properties.RsaProperties;
import com.koh.rsa.provider.RsaConfigProvider;
import com.koh.rsa.provider.RsaExceptionProvider;
import com.koh.rsa.service.RsaService;
import com.koh.rsa.util.RsaSignatureUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * RSA签名和验签处理器
 * aop 拦截所有请求，根据配置的RSA模式对请求验签或对响应加签
 */
@Aspect
@Component
@ConditionalOnProperty(prefix = "apcp.rsa", name = "enable", havingValue = "true")
public class RsaValidatorHandler {

    @Autowired
    private RsaProperties rsaProperties;
    @Autowired
    private RsaService rsaService;
    @Autowired(required = false)
    private RsaConfigProvider rsaConfigProvider;
    @Autowired(required = false)
    private RsaExceptionProvider rsaExceptionProvider;

    /**
     * 初始化
     */
    public RsaValidatorHandler() {
        rsaExceptionProvider = rsaExceptionProvider == null ? RsaExceptionProvider.defaultProvider() : rsaExceptionProvider;
    }

    /**
     * 切点 controller 方法
     */
    @Pointcut("within(*..controller..*)")
    public void anyControllerMethod() {
    }

    /**
     * 对请求进行验签
     *
     * @throws RuntimeException 验签异常
     */
    @Before("anyControllerMethod()")
    public void rsaCheck(JoinPoint joinPoint) {
        String rsaMode = getRsaMode(joinPoint);
        List<String> requireRsa = Arrays.asList(RsaMode.BOTH.name(), RsaMode.REQUEST_ONLY.name());
        if (!requireRsa.contains(rsaMode)) {
            return;
        }
        joinPoint.getArgs();
        String rsaContent = Optional.ofNullable(rsaConfigProvider)
                .map(RsaConfigProvider::getRsaContent)
                .filter(content -> !ObjectUtils.isEmpty(content))
                .orElseThrow(() -> rsaExceptionProvider.checkRsaParamException("签名内容为空，请检查配置或自定义实现"));
        String sign = Optional.ofNullable(rsaConfigProvider)
                .map(RsaConfigProvider::getSign)
                .filter(content -> !content.isEmpty())
                .orElseThrow(() -> rsaExceptionProvider.checkRsaParamException("签名为空，请检查配置或自定义实现"));
        String publicKey = Optional.ofNullable(rsaService.getPublicKey()).orElseThrow(() -> rsaExceptionProvider.checkRsaParamException("公钥获取失败,请检查配置或自定义实现"));
        String signType = Optional.ofNullable(rsaService.getSignType()).orElseThrow(() -> rsaExceptionProvider.checkRsaParamException("签名类型获取失败,请检查配置或自定义实现"));
        boolean verify = RsaSignatureUtil.verify(rsaContent, sign, publicKey, signType, rsaExceptionProvider);
        if (!verify) {
            throw rsaExceptionProvider.rsaSignException("验签失败，请检查签名内容或公钥");
        }
    }

    /**
     * 对响应内容进行加签
     * 对data内容加签，放入返回中，和code，message同级
     *
     * @param joinPoint 切点
     * @return 返回值
     * @throws Throwable 异常
     */
    @Around("anyControllerMethod()")
    public Object rsaSign(ProceedingJoinPoint joinPoint) throws Throwable {
        String rsaMode = getRsaMode(joinPoint);
        Object proceed = joinPoint.proceed();
        List<String> requireRsa = Arrays.asList(RsaMode.BOTH.name(), RsaMode.RESPONSE_ONLY.name());
        if (requireRsa.contains(rsaMode)) {
            proceed = Optional.ofNullable(rsaConfigProvider.getRsaResult(proceed))
                    .orElseThrow(() -> rsaExceptionProvider.checkRsaParamException("开启了响应加签，但未获取到加签结果，请检查配置或自定义实现"));
        }
        return proceed;
    }

    /**
     * 获取RSA模式
     *
     * @param joinPoint 切点
     * @return 返回模式
     */
    private String getRsaMode(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RsaSecure rsaSecure = method.getAnnotation(RsaSecure.class);
        return Optional.ofNullable(rsaSecure)
                .map(RsaSecure::rsaMode)
                .map(RsaMode::name)
                .orElse(rsaProperties.getDefaultRsaMode());
    }
}
