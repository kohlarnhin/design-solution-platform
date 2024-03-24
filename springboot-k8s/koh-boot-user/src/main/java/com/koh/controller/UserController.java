package com.koh.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    /**
     * 通过用户id查询
     */
    @GetMapping("/user/{id}")
    public String getUserById(@PathVariable Long id) {
        return "user id: " + id;
    }
}
