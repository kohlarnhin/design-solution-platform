package com.koh.mqtt;

import com.koh.mqtt.properties.MqttProperties;
import com.koh.mqtt.properties.WillProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(value = {"com.koh.mqtt"})
@EnableConfigurationProperties({MqttProperties.class, WillProperties.class})
public class MqttAutoConfiguration {

}
