package com.koh.controller;

import com.koh.entity.User;
import com.koh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    private final UserService userService;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @GetMapping("/config")
    public String getConfig() {
        return datasourceUrl;
    }


    @GetMapping("/services")
    public List<String> services() {
        System.out.println(discoveryClient);
        return this.discoveryClient.getServices();
    }
}
