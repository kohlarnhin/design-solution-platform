package com.koh.mqtt.config;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MyAutoClose implements AutoCloseable {

    @Autowired(required = false)
    private MqttClient mqttClient;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void close() throws MqttException {
        if (mqttClient != null) {
            mqttClient.disconnect();
            mqttClient.close();
            logger.info("MQTT客户端关闭成功");
        }
    }
}
