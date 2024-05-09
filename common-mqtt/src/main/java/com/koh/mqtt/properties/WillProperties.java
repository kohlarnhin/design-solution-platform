package com.koh.mqtt.properties;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Data
@ConfigurationProperties(prefix = "apcp.mqtt.will")
public class WillProperties {

    /**
     * 是否开启遗嘱消息
     * 默认false
     */
    private boolean enable = false;

    /**
     * 主题
     */
    @NotBlank(message = "遗嘱消息主题不能为空")
    private String topic;
    /**
     * 消息内容.
     */
    @NotBlank(message = "遗嘱消息内容不能为空")
    private String payload;
    /**
     * 消息QOS.
     * 默认1
     */
    @Min(value = 0, message = "qos只能为0、1、2")
    @Max(value = 2, message = "qos只能为0、1、2")
    private int qos = 1;
    /**
     * 消息是否保留
     * 默认false
     */
    private boolean retained = false;
}
