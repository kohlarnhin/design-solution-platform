package com.koh.rsa.provider;

import org.springframework.stereotype.Component;

/**
 * 实现自定义接口，不提供则该包下的所有异常都会是RUNTIME异常
 * 所有自定义异常都会有需要Throwable以及不需要的两个方法
 * <p>
 * Rsa校验参数异常接口
 * Rsa验签或加签异常接口
 */
@Component
public interface RsaExceptionProvider {

    default RuntimeException checkRsaParamException(String message, Throwable cause) {
        return new RuntimeException(message, cause);
    }

    default RuntimeException checkRsaParamException(String message) {
        return new RuntimeException(message);
    }

    default RuntimeException rsaSignException(String message, Throwable cause) {
        return new RuntimeException(message, cause);
    }

    default RuntimeException rsaSignException(String message) {
        return new RuntimeException(message);
    }

    static RsaExceptionProvider defaultProvider() {
        return new RsaExceptionProvider() {};
    }

}
