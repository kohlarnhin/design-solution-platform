package com.koh.log;

import com.tedia.apcp.log.properties.LogAsyncThreadPoolProperties;
import com.tedia.apcp.log.properties.LogProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(value = {"com.tedia.apcp.log"})
@EnableConfigurationProperties({LogAsyncThreadPoolProperties.class, LogProperties.class})
public class LogCommonAutoConfiguration {
}
