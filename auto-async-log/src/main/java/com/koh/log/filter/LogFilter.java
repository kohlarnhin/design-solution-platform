package com.koh.log.filter;

import com.koh.log.constants.LogConstants;
import com.koh.log.entity.LogInfo;
import com.koh.log.properties.LogProperties;
import com.koh.log.service.LogAsyncService;
import com.koh.log.util.ThreadMdcUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Component
@WebFilter(urlPatterns = "/*")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LogFilter extends OncePerRequestFilter {

    @Autowired
    private LogAsyncService logAsyncService;
    @Autowired
    private LogProperties logProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (logProperties.isEnable()) {
            // 创建请求和响应的包装器，可多次读取请求和响应的内容
            ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
            ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
            // 在 MDC中设置requestId
            setRequestIdInMDC();
            // 继续执行
            filterChain.doFilter(requestWrapper, responseWrapper);
            try {
                // 记录请求和响应的日志
                logRequestAndResponse(requestWrapper, responseWrapper);
            } finally {
                // 响应返回
                responseWrapper.copyBodyToResponse();
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private void setRequestIdInMDC() {
        try {
            ThreadMdcUtil.setRequestIdIfAbsent();
        } catch (Exception e) {
            LogInfo logInfo = new LogInfo();
            logInfo.setHttpStatus(HttpStatus.BAD_REQUEST.value());
            logInfo.setErrorMsg("生成request_id存入MDC异常：" + e.getMessage());
            logInfo.setLevel(LogConstants.ERROR_LEVEL);
            logAsyncService.printLogInfo(logInfo, String.format("错误信息打印：%s", logInfo));
        }
    }

    private void logRequestAndResponse(ContentCachingRequestWrapper requestWrapper, ContentCachingResponseWrapper responseWrapper) {
        try {
            // 从请求中生成日志信息
            LogInfo requestLogInfo = logAsyncService.createLogInfoFromRequest(requestWrapper);
            // 打印请求日志
            logAsyncService.handleRequest(requestLogInfo);
            // 打印响应日志
            logAsyncService.handleResponse(responseWrapper.getStatus(),
                    responseWrapper.getContentAsByteArray(), responseWrapper.getContentType());
        } catch (Exception e) {
            LogInfo logInfo = new LogInfo();
            logInfo.setHttpStatus(HttpStatus.BAD_REQUEST.value());
            logInfo.setErrorMsg("获取请求参数异常：" + e.getMessage());
            logInfo.setLevel(LogConstants.ERROR_LEVEL);
            logAsyncService.printLogInfo(logInfo, String.format("错误信息打印：%s", logInfo));
        }
    }
}
