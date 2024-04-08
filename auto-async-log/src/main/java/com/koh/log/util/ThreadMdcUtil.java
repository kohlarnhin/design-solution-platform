package com.koh.log.util;

import com.koh.log.constants.LogConstants;
import org.slf4j.MDC;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * MDC，添加 request_id
 */
public class ThreadMdcUtil {

    /**
     * 设置请求唯一 ID
     */
    public static void setRequestIdIfAbsent() {
        if (MDC.get(LogConstants.REQUEST_ID) == null) {
            MDC.put(LogConstants.REQUEST_ID, UUID.randomUUID().toString().replace("-", ""));
        }
    }

    public static <T> Callable<T> wrap(final Callable<T> callable, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
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
            try {
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }
}
