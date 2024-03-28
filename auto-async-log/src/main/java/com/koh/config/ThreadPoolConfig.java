package com.koh.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class ThreadPoolConfig {

    @Bean(name = "customThreadPool")
    public ThreadPoolExecutorMdcWrapper  taskExecutor() {
        ThreadPoolExecutorMdcWrapper executor = new ThreadPoolExecutorMdcWrapper();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setKeepAliveSeconds(5000);
        executor.setThreadNamePrefix("CustomThreadPool-");
        executor.initialize();
        return executor;
    }
}
