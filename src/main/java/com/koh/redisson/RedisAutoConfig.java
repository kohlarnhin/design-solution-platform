package com.koh.redisson;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "redisson.enabled", matchIfMissing = true)
public class RedisAutoConfig {

    @Bean
    @ConditionalOnMissingBean
    public RedissonProperties redissonProperties() {
        return new RedissonProperties();
    }


    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson(RedissonProperties redissonProperties) {
        Config config = new Config();
//        Redis url should start with redis:// or rediss:// (for SSL connection)
        config.useSingleServer()
                .setDatabase(redissonProperties.getDatabase())
                .setAddress("redis://"+redissonProperties.getAddress())
                .setPassword(redissonProperties.getPassword());
        // 根据config创建出RedissonClient实例
        return Redisson.create(config);
    }
}
