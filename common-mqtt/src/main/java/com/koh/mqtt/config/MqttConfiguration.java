package com.koh.mqtt.config;

import com.koh.mqtt.callback.CustomMqttCallback;
import com.koh.mqtt.callback.DefaultMqttCallbackImpl;
import com.koh.mqtt.properties.MqttProperties;
import com.koh.mqtt.properties.WillProperties;
import com.koh.mqtt.utils.ValidateObjectUtil;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MqttConfiguration {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private MqttAsyncClient mqttClient;
    @Autowired
    private MqttProperties mqttProperties;
    @Autowired
    private WillProperties willProperties;
    @Autowired(required = false)
    private CustomMqttCallback customMqttCallback;

    /**
     * 初始化
     */
    @Bean
    @ConditionalOnProperty(prefix = "apcp.mqtt", name = "enable", havingValue = "true")
    public MqttAsyncClient mqttClient() throws MqttException {
        ValidateObjectUtil.validate(mqttProperties);
        mqttClient = new MqttAsyncClient(mqttProperties.getBroker(), mqttProperties.getClientId() == null ? MqttClient.generateClientId() : mqttProperties.getClientId(), new MemoryPersistence());
        MqttConnectOptions options = getMqttConnectOptions();
        //没有自定义回调实现则使用默认回调
        mqttClient.setCallback(customMqttCallback == null ? new DefaultMqttCallbackImpl() : customMqttCallback);
        mqttClient.connect(options);
        logger.info("MQTT客户端初始化成功并连接");
        return mqttClient;
    }

    /**
     * 获取连接配置
     *
     * @return 连接配置
     */
    private MqttConnectOptions getMqttConnectOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(mqttProperties.getUsername());
        options.setPassword(mqttProperties.getPassword().toCharArray());
        options.setConnectionTimeout(mqttProperties.getConnectionTimeout());
        options.setKeepAliveInterval(mqttProperties.getKeepAliveInterval());
        //自动重连
        options.setAutomaticReconnect(true);
        //重启不清除session，需要保持clientId不变
        if (!mqttProperties.isCleanSession() && mqttProperties.getClientId() != null) {
            options.setCleanSession(false);
            logger.info("cleanSession设置为false，clientId: {}", mqttProperties.getClientId());
        }
        //设置遗嘱消息
        if (willProperties.isEnable()) {
            ValidateObjectUtil.validate(willProperties);
            options.setWill(willProperties.getTopic(), willProperties.getPayload().getBytes(), willProperties.getQos(), willProperties.isRetained());
            logger.info("遗嘱消息设置成功，topic: {}, payload: {}, qos: {}, retained: {}", willProperties.getTopic(), willProperties.getPayload(), willProperties.getQos(), willProperties.isRetained());
        }
        return options;
    }

}
