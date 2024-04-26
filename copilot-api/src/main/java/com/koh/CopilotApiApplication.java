package com.koh;

import com.koh.properties.CopilotProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({CopilotProperties.class})
public class CopilotApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(CopilotApiApplication.class, args);
    }
}