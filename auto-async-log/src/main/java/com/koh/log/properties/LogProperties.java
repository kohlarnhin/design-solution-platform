package com.koh.log.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "apcp.log")
public class LogProperties {
    private boolean enable = false;
}