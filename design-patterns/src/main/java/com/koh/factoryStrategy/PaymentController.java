package com.koh.factoryStrategy;


import com.koh.factoryStrategy.strategy.PaymentStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class PaymentController {

    @Autowired
//    @Qualifier("paymentStrategyMap")
    private Map<String,PaymentStrategy> paymentStrategyMap;

    @GetMapping("/pay/{type}")
    public String pay(@PathVariable("type") String type) {
        double amount = 100.0;
        PaymentStrategy paymentStrategy = paymentStrategyMap.get(type);
        paymentStrategy.pay(amount);
        return "";
    }
}
