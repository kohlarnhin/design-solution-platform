package com.koh.factoryStrategy.config;

import com.koh.factoryStrategy.strategy.AlipayPaymentStrategy;
import com.koh.factoryStrategy.strategy.PaymentStrategy;
import com.koh.factoryStrategy.strategy.WechatPaymentStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class PaymentStrategyConfig {

//    @Bean(name = "paymentStrategyMap")
//    public Map<String, PaymentStrategy> paymentStrategyMap() {
//        Map<String, PaymentStrategy> strategies = new HashMap<>();
//        strategies.put("alipay", new AlipayPaymentStrategy());
//        strategies.put("wxpay", new WechatPaymentStrategy());
//        // Add your payment strategies to the map here
//        return strategies;
//    }
}
