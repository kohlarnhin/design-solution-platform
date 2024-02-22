package com.koh.factoryStrategy.strategy;

import org.springframework.stereotype.Component;

@Component("alipay")
public class AlipayPaymentStrategy implements PaymentStrategy{
    @Override
    public void pay(double amount) {
        System.out.println("Paid " + amount + " RMB via Alipay.");
    }
}
