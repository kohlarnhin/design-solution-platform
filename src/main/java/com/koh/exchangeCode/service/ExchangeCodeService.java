package com.koh.exchangeCode.service;

import com.koh.exchangeCode.bloomFilter.BloomFilterService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
public class ExchangeCodeService {

    private static final int CODE_LENGTH = 12;
    private static final String CODE_CHARACTERS = "0123456789";

    private static final String BLOOM_FILTER_NAME = "exchangeCodeBloomFilter";

    private static final int MAX_RETRY_COUNT = 0;

    @Autowired
    private BloomFilterService bloomFilterService;

    /**
     * 初始化布隆过滤器
     */
//    @PostConstruct
    public void initBoomFilter() {
//        bloomFilterService.deleteBloomFilter(BLOOM_FILTER_NAME);
        bloomFilterService.initializeBloomFilter(BLOOM_FILTER_NAME, 10000, 0.01);
    }

    /**
     * 生成兑换码
     *
     * @return 生成的兑换码
     */
    public String generateExchangeCode() {
        int retryCount = 0;
        String code;
        boolean addedSuccessfully;
        do {
            code = generateRandomCode();

            // 添加元素到布隆过滤器，并检查是否添加成功
            addedSuccessfully = bloomFilterService.addElement(BLOOM_FILTER_NAME, code);

            // 如果添加不成功，增加重试计数，并在达到最大重试次数时打印失败日志
            if (!addedSuccessfully) {
                retryCount++;
                if (MAX_RETRY_COUNT == 0 || retryCount >= MAX_RETRY_COUNT) {
                    log.error("无法生成唯一兑换码，达到最大重试次数: {}", code);
                }
            }
        } while (!addedSuccessfully);
        log.info("生成兑换码成功: {}", code);
        return code;
    }

    /**
     * 生成指定长度的随机码
     *
     * @return 生成的随机码
     */
    private String generateRandomCode() {
        Random random = new Random();
        StringBuilder codeBuilder = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CODE_CHARACTERS.length());
            codeBuilder.append(CODE_CHARACTERS.charAt(index));
        }
        return codeBuilder.toString();
    }
}
