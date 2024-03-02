package com.koh.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisLockUtil {

    private static final String LOCK_PREFIX = "lock:";
    private static final long DEFAULT_EXPIRE = 30; // 默认过期时间，单位为秒

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 尝试获取分布式锁
     * @param key 锁的键
     * @param value 锁的值
     * @return 是否获取成功
     */
    public boolean tryLock(String key, String value) {
        return tryLock(key, value, DEFAULT_EXPIRE);
    }

    /**
     * 尝试获取分布式锁
     * @param key 锁的键
     * @param value 锁的值
     * @param expire 过期时间，单位为秒
     * @return 是否获取成功
     */
    public boolean tryLock(String key, String value, long expire) {
        String lockKey = LOCK_PREFIX + key;
        Boolean result = redisTemplate.opsForValue().setIfAbsent(lockKey, value, expire, TimeUnit.SECONDS);
        return result != null && result;
    }

    /**
     * 释放分布式锁
     * @param key 锁的键
     * @param value 锁的值
     * @return 是否释放成功
     */
    public boolean releaseLock(String key, String value) {
        String lockKey = LOCK_PREFIX + key;
        String lockValue = redisTemplate.opsForValue().get(lockKey);
        if (lockValue != null && lockValue.equals(value)) {
            return redisTemplate.delete(lockKey);
        }
        return false;
    }
}
