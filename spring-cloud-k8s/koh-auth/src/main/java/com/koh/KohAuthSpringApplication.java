package com.koh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class KohAuthSpringApplication {
    public static void main(String[] args) {
        SpringApplication.run(KohAuthSpringApplication.class, args);
    }
}
