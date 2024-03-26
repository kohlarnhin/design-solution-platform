//package com.koh.config;
//
//import org.checkerframework.checker.units.qual.C;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class CorsConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**") // 允许跨域的路径
//                .allowedOrigins("*") // 允许跨域请求的域名
//                .allowedMethods("GET", "POST", "PUT", "DELETE") // 允许的请求方法
//                .allowedHeaders("*") // 允许的请求头
//                .allowCredentials(true); // 是否允许证书（cookies）
//    }
//}
