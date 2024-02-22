package com.koh.exchangeCode;

import com.koh.DesignSolutionPlatformApplicationTests;
import com.koh.exchangeCode.bloomFilter.BloomFilterService;
import com.koh.exchangeCode.service.ExchangeCodeService;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CountDownLatch;

public class ExchangeCodeTest extends DesignSolutionPlatformApplicationTests {

    private static final String BLOOM_FILTER_NAME = "exchangeCodeBloomFilter";
    private static final String NEW_BLOOM_FILTER_NAME = "newExchangeCodeBloomFilter";

    @Autowired
    private ExchangeCodeService exchangeCodeService;

    @Autowired
    private BloomFilterService bloomFilterService;

    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void generateExchangeCode() {
//        bloomFilterService.deleteBloomFilter(BLOOM_FILTER_NAME);
        bloomFilterService.initializeBloomFilter(BLOOM_FILTER_NAME, 10000, 0.01);
        CountDownLatch countDownLatch = new CountDownLatch(1000);
        //开始时间
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                exchangeCodeService.generateExchangeCode();
                countDownLatch.countDown();
            }).start();
        }
        //等待所有线程执行完
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("生成1000个兑换码耗时：" + (end - start) + "ms");

        RBloomFilter<Object> oldBloomFilter = redissonClient.getBloomFilter(BLOOM_FILTER_NAME);
        long count = oldBloomFilter.count();
        System.out.println(count);

        // 创建新的布隆过滤器
        bloomFilterService.initializeBloomFilter(NEW_BLOOM_FILTER_NAME, 20000, 0.01);
        oldBloomFilter
    }
}
