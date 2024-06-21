package com.koh.rsa;


import com.koh.rsa.properties.RsaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(value = {"com.koh.rsa"})
@EnableConfigurationProperties(value = {RsaProperties.class})
public class RsaConfiguration {

}
