package com.koh.mqtt.properties;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Data
@ConfigurationProperties(prefix = "apcp.mqtt")
public class MqttProperties {

    /**
     * 是否启用
     */
    private boolean enable = false;

    /**
     * 服务器
     */
    @NotBlank(message = "服务器不能为空")
    private String broker;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 客户端ID
     */
    private String clientId;

    /**
     * 会话是否清除
     * 默认true 清除 重启后服务端不会保留客户端的连接信息
     * 设置false 保留 重启后服务端会保留客户端的连接信息 需要保证clientId不变
     * false需要clientId不为null否则默认true
     */
    private boolean cleanSession = true;

    /**
     * 连接超时时间
     * 默认60s
     */
    private int connectionTimeout = 60;

    /**
     * 保持连接时间
     * 默认60s
     */
    private int keepAliveInterval = 60;
}
