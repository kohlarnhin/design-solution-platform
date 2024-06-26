package com.koh;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.koh.mapper")
public class KohUserSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(KohUserSpringBootApplication.class, args);
    }

}
