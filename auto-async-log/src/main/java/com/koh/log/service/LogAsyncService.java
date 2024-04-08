package com.koh.log.service;

import com.alibaba.fastjson2.JSONObject;
import com.koh.log.constants.LogConstants;
import com.koh.log.entity.LogInfo;
import com.koh.log.entity.Result;
import org.apache.tomcat.util.buf.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LogAsyncService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Async("asyncPrintLogPool")
    public void handleRequest(LogInfo requestLogInfo) {
        requestLogInfo.setLevel(LogConstants.INFO_LEVEL);
        printLogInfo(requestLogInfo, String.format("请求打印日志：%s", requestLogInfo));
    }

    @Async("asyncPrintLogPool")
    public void handleResponse(int httpStatus, byte[] body, String contentType) {
        LogInfo responseLogInfo = null;
        try {
            String bodyString = new String(body, Charset.defaultCharset());
            if (HttpStatus.OK.value() != httpStatus) {
                responseLogInfo = new LogInfo();
                responseLogInfo.setLevel(LogConstants.ERROR_LEVEL);
                responseLogInfo.setHttpStatus(httpStatus);
                responseLogInfo.setErrorMsg("响应服务异常: " + bodyString);
            } else {
                // 类型是application/json才会进行下面的处理
                if (MediaType.APPLICATION_JSON_VALUE.equals(contentType)) {
                    Result<?> result = JSONObject.parseObject(bodyString, Result.class);
                    if (0 != result.getCode()) {
                        responseLogInfo = new LogInfo();
                        responseLogInfo.setLevel(LogConstants.ERROR_LEVEL);
                        responseLogInfo.setResultCode(result.getCode());
                        responseLogInfo.setErrorMsg("响应错误信息：" + result.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            responseLogInfo = new LogInfo();
            responseLogInfo.setLevel(LogConstants.ERROR_LEVEL);
            responseLogInfo.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseLogInfo.setErrorMsg("响应获取内容异常: " + e.getMessage());
        } finally {
            if (!Objects.isNull(responseLogInfo)) {
                printLogInfo(responseLogInfo, String.format("错误信息打印：%s", responseLogInfo));
            }
        }
    }

    public LogInfo createLogInfoFromRequest(ContentCachingRequestWrapper requestWrapper) {
        LogInfo logInfo = new LogInfo();
        logInfo.setRequestId(MDC.get(LogConstants.REQUEST_ID));
        logInfo.setRequestTime(LocalDateTime.now(ZoneOffset.UTC));
        logInfo.setUsername(getUsername());
        logInfo.setRequestIP(requestWrapper.getRemoteAddr());
        logInfo.setRequestUrl(requestWrapper.getRequestURL().toString());
        logInfo.setRequestUri(requestWrapper.getRequestURI());
        logInfo.setRequestMethod(requestWrapper.getMethod());
        logInfo.setContentType(requestWrapper.getContentType());
        logInfo.setRequestParams(getRequestParams(requestWrapper));
        logInfo.setRequestBody(getRequestBody(requestWrapper));
        return logInfo;
    }

    private String getRequestParams(ContentCachingRequestWrapper requestWrapper) {
        Map<String, String> requestParamsMap = getRequestParamsMap(requestWrapper);
        return requestParamsMap.isEmpty() ? null : JSONObject.toJSONString(requestParamsMap);
    }

    private Map<String, String> getRequestParamsMap(ContentCachingRequestWrapper requestWrapper) {
        Map<String, String[]> parameterMap = Optional.of(requestWrapper.getParameterMap()).orElse(Collections.emptyMap());
        Map<String, String> pathVariables = Optional.ofNullable((Map<String, String>) requestWrapper.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE)).orElse(Collections.emptyMap());
        Map<String, String> requestParams = parameterMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().length == 1 ? e.getValue()[0] : StringUtils.join(List.of(e.getValue()), ',')));
        requestParams.putAll(pathVariables);
        return requestParams;
    }

    private String getRequestBody(ContentCachingRequestWrapper requestWrapper) {
        byte[] content = requestWrapper.getContentAsByteArray();
        return content.length == 0 ? null : new String(content, Charset.defaultCharset());
    }

    public void printLogInfo(LogInfo logInfo, String message) {
        Map<String, String> fieldsForMDC;
        try {
            fieldsForMDC = logInfo.getFieldsValueForMDC();
            fieldsForMDC.forEach(MDC::put);
            if (LogConstants.INFO_LEVEL.equals(logInfo.getLevel())) {
                logger.info(message);
            } else if (LogConstants.ERROR_LEVEL.equals(logInfo.getLevel())) {
                logger.error(message);
            }
        } catch (Exception e) {
            logger.error("打印日志方法错误: {}", e.getMessage());
        } finally {
            // 清除MDC字段
            LogInfo.getFieldsToRemoveMDC().forEach(MDC::remove);
        }
    }

    private String getUsername() {
        return "具体实现";
    }
}

