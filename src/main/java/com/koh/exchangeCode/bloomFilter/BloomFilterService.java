package com.koh.exchangeCode.bloomFilter;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BloomFilterService {

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 初始化布隆过滤器
     *
     * @param bloomFilterName          布隆过滤器名称
     * @param expectedInsertions       预期插入的元素数量
     * @param falsePositiveProbability 期望的误判率
     */
    public void initializeBloomFilter(String bloomFilterName, long expectedInsertions, double falsePositiveProbability) {
        // 获取或创建 Redisson 布隆过滤器
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter(bloomFilterName);

        // 初始化布隆过滤器，设置预期插入元素数量和误判率
        bloomFilter.tryInit(expectedInsertions, falsePositiveProbability);
    }

    /**
     * 新增元素到布隆过滤器
     *
     * @param bloomFilterName 布隆过滤器名称
     * @param element         要添加的元素
     */
    public boolean addElement(String bloomFilterName, String element) {
        // 获取 Redisson 布隆过滤器
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter(bloomFilterName);

        // 添加元素到布隆过滤器
        return bloomFilter.add(element);
    }

    /**
     * 判断元素是否存在于布隆过滤器
     *
     * @param bloomFilterName 布隆过滤器名称
     * @param element         要判断的元素
     * @return 如果元素可能存在于布隆过滤器，返回 true；否则返回 false
     */
    public boolean containsElement(String bloomFilterName, String element) {
        // 获取 Redisson 布隆过滤器
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter(bloomFilterName);

        // 判断元素是否存在于布隆过滤器
        return bloomFilter.contains(element);
    }

    /**
     * 删除布隆过滤器
     *
     * @param bloomFilterName 布隆过滤器名称
     */
    public void deleteBloomFilter(String bloomFilterName) {
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter(bloomFilterName);
        bloomFilter.delete();
    }

    /**
     * 获取布隆过滤器的容量
     *
     * @param bloomFilterName 获取布隆过滤器的容量
     */
    public long count(String bloomFilterName) {
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter(bloomFilterName);
        return bloomFilter.getExpectedInsertions();
    }



    /**
     * 获取布隆过滤器信息
     *
     * @param bloomFilterName 布隆过滤器名称
     * @return 布隆过滤器信息的字符串表示
     */
    public String getBloomFilterInfo(String bloomFilterName) {
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter(bloomFilterName);

        // 获取期望插入数量
        long expectedInsertions = bloomFilter.getExpectedInsertions();

        // 获取实际元素数量
        long numberOfElements = bloomFilter.getSize();

        // 获取误报率（通过计算）
        double falsePositiveProbability = calculateFalsePositiveProbability(expectedInsertions, numberOfElements, bloomFilter.getHashIterations());

        // 返回布隆过滤器信息的字符串表示
        return String.format(
                "Bloom Filter Information: Expected Insertions = %d, False Positive Probability = %f, Number of Elements = %d",
                expectedInsertions, falsePositiveProbability, numberOfElements
        );
    }

    // 计算误报率的方法
    private double calculateFalsePositiveProbability(long expectedInsertions, long numberOfElements, long hashIterations) {
        return Math.pow(1 - Math.exp(-(double) hashIterations * (double) numberOfElements / (double) expectedInsertions), (double) hashIterations);
    }
}
