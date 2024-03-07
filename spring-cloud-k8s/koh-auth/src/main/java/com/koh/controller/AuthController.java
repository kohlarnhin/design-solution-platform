package com.koh.controller;

import com.koh.entity.User;
import com.koh.feign.UserFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/auth")
@RestController
public class AuthController {

    @Autowired
    private UserFeign userFeign;


    /**
     * 登录接口
     */
    @RequestMapping("/login")
    public User login() {
        User userById = userFeign.getUserById(1L);
        return userById;
    }
}
