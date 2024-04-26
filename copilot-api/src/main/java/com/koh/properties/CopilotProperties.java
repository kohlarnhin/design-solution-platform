package com.koh.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "copilot")
public class CopilotProperties {

    /**
     * 凭证
     */
    private String ghu;

    /**
     * 密码key
     */
    private String key;

}
