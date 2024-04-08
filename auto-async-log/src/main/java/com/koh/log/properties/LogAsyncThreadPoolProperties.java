package com.koh.log.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "apcp.log.thread.pool")
public class LogAsyncThreadPoolProperties {

    /**
     * 核心线程数量
     */
    private int coreSize = 1;

    /**
     * 最大线程数量
     */
    private int maxSize = 4;

    /**
     * 队列数量
     */
    private int queueCapacity = 64;

    /**
     * 活跃时间
     */
    private int keepAliveSeconds = 300;
}
