package com.koh.factoryStrategy.strategy;

import org.springframework.stereotype.Component;

@Component("wxpay")
public class WechatPaymentStrategy implements PaymentStrategy{
    @Override
    public void pay(double amount) {
        System.out.println("Paid " + amount + " RMB via Wechat.");
    }
}
