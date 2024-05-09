package com.koh.mqtt.entity;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


/**
 * MQTT实体类
 */
@Data
public class MqttMessageEntity {

    /**
     * 内容
     */
    @NotBlank(message = "内容不能为空")
    private String content;

    /**
     * 消息等级
     * QoS 0，最多交付一次。消息可能会丢失。
     * QoS 1，至少交付一次。消息可能会重复。
     * QoS 2，只交付一次。消息绝不会重复。成本高。
     * 默认为1，只能为0、1、2
     */
    @Min(value = 0, message = "qos只能为0、1、2")
    @Max(value = 2, message = "qos只能为0、1、2")
    private int qos = 1;

    /**
     * 主题
     */
    @NotBlank(message = "主题不能为空")
    private String topic;

    /**
     * 是否保留 默认不保留
     * 保留，新的订阅者会收到最后一条保留消息
     * 不保留，新的订阅者不会收到消息
     */
    private boolean retained = false;
}
