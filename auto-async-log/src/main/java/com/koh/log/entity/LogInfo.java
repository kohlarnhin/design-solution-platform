package com.koh.log.entity;

import com.koh.log.annotation.EsFieldName;
import com.koh.log.annotation.IncludeInMDC;
import lombok.Data;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Data
public class LogInfo {

    /**
     * 请求ID
     */
    @IncludeInMDC
    @EsFieldName("request_id")
    private String requestId;

    /**
     * 请求时间
     */
    @IncludeInMDC
    @EsFieldName("request_time")
    private LocalDateTime requestTime;

    /**
     * 用户
     */
    @IncludeInMDC
    @EsFieldName("username")
    private String username;

    /**
     * 请求IP
     */
    @IncludeInMDC
    @EsFieldName("request_ip")
    private String requestIP;

    /**
     * 请求URL
     */
    @IncludeInMDC
    @EsFieldName("request_url")
    private String requestUrl;

    /**
     * 请求URI
     */
    @IncludeInMDC
    @EsFieldName("request_uri")
    private String requestUri;

    /**
     * 请求方式
     */
    @IncludeInMDC
    @EsFieldName("request_method")
    private String requestMethod;

    /**
     * 请求参数
     */
    @IncludeInMDC
    @EsFieldName("request_params")
    private String requestParams;

    /**
     * 请求类型
     */
    @IncludeInMDC
    @EsFieldName("content_type")
    private String contentType;

    /**
     * 请求体
     */
    @IncludeInMDC
    @EsFieldName("request_body")
    private String requestBody;

    /**
     * 错误信息
     */
    @IncludeInMDC
    @EsFieldName("error_msg")
    private String errorMsg;

    /**
     * 状态码
     */
    @IncludeInMDC
    @EsFieldName("http_status")
    private Integer httpStatus;

    /**
     * 结果
     */
    @IncludeInMDC
    @EsFieldName("result_code")
    private Integer resultCode;

    /**
     * 打印级别
     */
    private String level;

    /**
     * 需要存入MDC的字段
     */
    private static List<Field> mdcFields;

    /**
     * 初始化
     */
    static {
        mdcFields = Arrays.stream(LogInfo.class.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(IncludeInMDC.class))
                .collect(Collectors.toList());
    }


    /**
     * 获取需要存入MDC的key和value
     * @return
     * @throws IllegalAccessException
     */
    public Map<String, String> getFieldsValueForMDC() throws IllegalAccessException {
        Map<String, String> fields = new HashMap<>();
        for (Field field : mdcFields) {
            field.setAccessible(true);
            Object value = field.get(this);
            if (!Objects.isNull(value)) {
                EsFieldName esFieldName = field.getAnnotation(EsFieldName.class);
                String fieldName = esFieldName != null ? esFieldName.value() : field.getName();
                fields.put(fieldName, value.toString());
            }
        }
        return fields;
    }

    /**
     * 获取需要从MDC中删除的字段名称，requestId除外
     * @return
     */
    public static List<String> getFieldsToRemoveMDC() {
        return mdcFields.stream().filter(field -> !field.getName().equals("requestId")).map(field -> {
            EsFieldName esFieldName = field.getAnnotation(EsFieldName.class);
            return esFieldName != null ? esFieldName.value() : field.getName();
        }).collect(Collectors.toList());
    }


    @Override
    public String toString() {
        List<String> parts = new ArrayList<>();
        if (requestId != null) parts.add("requestId=" + requestId);
        if (username != null) parts.add("username=" + username);
        if (requestIP != null) parts.add("requestIP=" + requestIP);
        if (requestUrl != null) parts.add("requestUrl=" + requestUrl);
        if (requestUri != null) parts.add("requestUri=" + requestUri);
        if (requestMethod != null) parts.add("requestMethod=" + requestMethod);
        if (requestParams != null && !requestParams.isEmpty()) parts.add("requestParams=" + requestParams);
        if (contentType != null) parts.add("contentType=" + contentType);
        if (requestBody != null) parts.add("requestBody=" + requestBody);
        if (errorMsg != null) parts.add("errorMsg=" + errorMsg);
        if (httpStatus != null) parts.add("httpStatus=" + httpStatus);
        if (resultCode != null) parts.add("resultCode=" + resultCode);
        return String.join(", ", parts);
    }

}
