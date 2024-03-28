package com.koh.service;


import com.koh.controller.TestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TestService {

    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    @Async(value = "customThreadPool")
    public void test() {
        log.info("test,service");
    }
}
