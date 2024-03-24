package com.koh.redis;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private RedissonClient redissonClient;

    @GetMapping("/redisson/test")
    public void test() {
        RLock lock = redissonClient.getLock("2023");
        lock.lock();
        System.out.println(lock);
    }
}
