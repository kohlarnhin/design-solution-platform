package com.koh.mqtt.callback;

import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;

/**
 * 自定义MqttCallback 不实现则用默认的DefaultMqttCallbackImpl
 */
public interface CustomMqttCallback extends MqttCallbackExtended {
}
