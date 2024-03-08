package com.koh.controller;

import com.koh.entity.User;
import com.koh.feign.UserFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/auth")
@RestController
public class AuthController {

    @Autowired
    private UserFeign userFeign;

    @Autowired
    private DiscoveryClient discoveryClient;


    /**
     * 登录接口
     */
    @RequestMapping("/login")
    public User login() {
        User userById = userFeign.getUserById(1L);
        return userById;
    }

    @GetMapping("/services")
    public List<String> services() {
        for (String service : discoveryClient.getServices()) {
            System.out.println(service);
        }
        List<ServiceInstance> instances = discoveryClient.getInstances("koh-user");
        return this.discoveryClient.getServices();
    }
}
