package com.koh.mqtt.service;


import com.koh.mqtt.entity.MqttMessageEntity;
import com.koh.mqtt.utils.ValidateObjectUtil;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class MqttService {

    /**
     * 打印日志
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired(required = false)
    private MqttAsyncClient mqttClient;

    /**
     * 发送消息
     */
    public IMqttDeliveryToken publish(MqttMessageEntity mqttMessageEntity) throws MqttException {
        checkMqttClientInitialized();
        ValidateObjectUtil.validate(mqttMessageEntity);
        MqttMessage message = new MqttMessage(mqttMessageEntity.getContent().getBytes());
        message.setQos(mqttMessageEntity.getQos());
        message.setRetained(mqttMessageEntity.isRetained());
        return mqttClient.publish(mqttMessageEntity.getTopic(), message);
    }

    /**
     * 订阅方法 只有主题
     *
     * @param topic 主题
     * @throws MqttException 异常
     */
    public void subscribe(String topic) throws MqttException {
        this.subscribe(new String[]{topic}, new int[]{1}, null);
    }

    /**
     * 订阅方法 主题和消息监听器
     *
     * @param topic 主题
     * @throws MqttException 异常
     */
    public void subscribe(String topic, IMqttMessageListener messageListener) throws MqttException {
        this.subscribe(new String[]{topic}, new int[]{1}, new IMqttMessageListener[]{messageListener});
    }

    /**
     * 订阅方法 主题和消息等级
     *
     * @param topic 主题
     * @param qos   消息等级
     * @throws MqttException 异常
     */
    public void subscribe(String topic, int qos) throws MqttException {
        this.subscribe(new String[]{topic}, new int[]{qos}, null);
    }

    /**
     * 订阅方法 主题和消息等级以及消息监听器
     *
     * @param topic           主题
     * @param qos             消息等级
     * @param messageListener 消息监听器
     * @throws MqttException 异常
     */
    public void subscribe(String topic, int qos, IMqttMessageListener messageListener) throws MqttException {
        this.subscribe(new String[]{topic}, new int[]{qos}, new IMqttMessageListener[]{messageListener});
    }

    /**
     * 订阅方法 只有主题 数组
     *
     * @param topics 主题数组
     * @throws MqttException 异常
     */
    public void subscribe(String[] topics) throws MqttException {
        this.subscribe(topics, new int[]{1}, null);
    }

    /**
     * 订阅方法 主题和消息监听器
     *
     * @param topics           主题 数组
     * @param messageListeners 消息监听器 数组
     * @throws MqttException 异常
     */
    public void subscribe(String[] topics, IMqttMessageListener[] messageListeners) throws MqttException {
        this.subscribe(topics, new int[]{1}, messageListeners);
    }

    /**
     * 订阅方法 主题和消息等级
     *
     * @param topics 主题 数组
     * @param qos    消息等级 数组
     * @throws MqttException 异常
     */
    public void subscribe(String[] topics, int[] qos) throws MqttException {
        this.subscribe(topics, qos, null);
    }

    /**
     * 取消订阅 主题
     *
     * @param topic 主题
     */
    public void unsubscribe(String topic) throws MqttException {
        checkMqttClientInitialized();
        this.unsubscribe(new String[]{topic});
    }

    /**
     * 订阅方法 主题和消息等级以及消息监听器 数组
     *
     * @param topics           主题
     * @param qos              消息等级
     * @param messageListeners 消息监听器
     * @throws MqttException 异常
     */
    public void subscribe(String[] topics, int[] qos, IMqttMessageListener[] messageListeners) throws MqttException {
        this.checkMqttClientInitialized();
        mqttClient.subscribe(topics, qos, messageListeners);
        logger.info("已订阅主题: {}, 消息等级: {}", Arrays.toString(topics), Arrays.toString(qos));
    }

    /**
     * 取消订阅 数组
     *
     * @param topics 主题数组
     */
    public void unsubscribe(String[] topics) throws MqttException {
        checkMqttClientInitialized();
        mqttClient.unsubscribe(topics);
        logger.info("已取消订阅主题: {}", Arrays.toString(topics));
    }

    private void checkMqttClientInitialized() {
        if (mqttClient == null) {
            throw new RuntimeException("MqttClient未初始化");
        }
    }
}
