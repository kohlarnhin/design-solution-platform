package com.koh.log;

import com.koh.log.properties.LogAsyncThreadPoolProperties;
import com.koh.log.properties.LogProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(value = {"com.koh"})
@EnableConfigurationProperties({LogAsyncThreadPoolProperties.class, LogProperties.class})
public class LogCommonAutoConfiguration {
}
