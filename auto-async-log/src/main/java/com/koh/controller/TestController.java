package com.koh.controller;


import com.koh.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class TestController {

    private static final Logger log = LoggerFactory.getLogger(TestController.class);
    @Autowired
    private TestService testService;

    @GetMapping("/test")
    public String test() {
        testService.test();
        log.info("test,controller");
        return "test";
    }
}
