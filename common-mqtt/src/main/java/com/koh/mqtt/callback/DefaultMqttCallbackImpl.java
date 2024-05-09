package com.koh.mqtt.callback;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultMqttCallbackImpl implements MqttCallbackExtended {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public DefaultMqttCallbackImpl() {
        logger.info("MqttCallbackExtendedDefaultImpl实例化");
    }

    // 连接丢失
    @Override
    public void connectionLost(Throwable cause) {
        logger.error("连接丢失", cause);
    }

    // 收到消息
    @Override
    public void messageArrived(String topic, MqttMessage message) {
        logger.info("\n 收到消息: \n  topic：{}\n  Qos：{}\n  payload：{}", topic, message.getQos(), new String(message.getPayload()));
    }

    // 消息传递成功
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        logger.info("消息已成功传递: {}", token.isComplete());
    }

    // 连接完成
    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
        if (reconnect) {
            logger.info("已重新连接到MQTT服务器: {}", serverURI);
        } else {
            logger.info("已首次连接到MQTT服务器: {}", serverURI);
        }
    }
}
