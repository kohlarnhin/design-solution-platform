package com.koh.log.config;

import com.koh.log.properties.LogAsyncThreadPoolProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.ThreadPoolExecutor;

@EnableAsync
@Configuration
public class LogAsyncThreadPool {

    @Autowired
    private LogAsyncThreadPoolProperties logAsyncThreadPoolProperties;

    @Bean
    public ThreadPoolExecutorMdcWrapper asyncPrintLogPool() {
        ThreadPoolExecutorMdcWrapper executor = new ThreadPoolExecutorMdcWrapper();;
        //核心线程池大小
        executor.setCorePoolSize(logAsyncThreadPoolProperties.getCoreSize());
        //最大线程数
        executor.setMaxPoolSize(logAsyncThreadPoolProperties.getMaxSize());
        //队列容量
        executor.setQueueCapacity(logAsyncThreadPoolProperties.getQueueCapacity());
        //活跃时间
        executor.setKeepAliveSeconds(logAsyncThreadPoolProperties.getKeepAliveSeconds());
        //线程名字前缀
        executor.setThreadNamePrefix("print-log-");
        //任务缓存队列满，线程数达到最大线程数，由主调线程执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
