package com.koh.config;

import org.slf4j.MDC;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * MDC 帮助类，添加 reqest_id
 */
public class ThreadMdcUtil {


    public static final String REQUEST_ID = "request_id";

    /**
     * 设置请求唯一 ID
     */
    public static void setTraceIdIfAbsent() {
        if (MDC.get(REQUEST_ID) == null) {
            MDC.put(REQUEST_ID, UUID.randomUUID().toString().replace("-", ""));
        }
    }

    /**
     * 存在 userId 则添加到 REQUEST_ID 中
     * @param userId
     */
    public static void setUserId(String userId) {
        String s = MDC.get(REQUEST_ID);
        if (s != null) {
            MDC.put(REQUEST_ID, s + "_" + userId);
        }
    }

    public static void removeTraceId() {
        MDC.remove(REQUEST_ID);
    }

    public static <T> Callable<T> wrap(final Callable<T> callable, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            setTraceIdIfAbsent();
            try {
                return callable.call();
            } finally {
                MDC.clear();
            }
        };
    }

    public static Runnable wrap(final Runnable runnable, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            // 设置 traceId
            setTraceIdIfAbsent();
            try {
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }
}
