package com.koh.factoryStrategy.factory;

import com.koh.factoryStrategy.strategy.AlipayPaymentStrategy;
import com.koh.factoryStrategy.strategy.PaymentStrategy;
import com.koh.factoryStrategy.strategy.WechatPaymentStrategy;
import com.koh.factoryStrategy.type.PaymentType;

import java.util.HashMap;
import java.util.Map;

public class PaymentFactory {

    private static final Map<String, PaymentStrategy> PAYMENT_STRATEGY_MAP = new HashMap<>();

    static {
        PAYMENT_STRATEGY_MAP.put(PaymentType.ALIPAY.getCode(), new AlipayPaymentStrategy());
        PAYMENT_STRATEGY_MAP.put(PaymentType.WECHATPAY.getCode(), new WechatPaymentStrategy());
    }

    public static PaymentStrategy getPaymentStrategy(int paymentCode) {
        PaymentStrategy paymentStrategy = PAYMENT_STRATEGY_MAP.get(paymentCode);
        if (paymentStrategy == null) {
            throw new IllegalArgumentException("Invalid payment code");
        }
        return paymentStrategy;
    }
}
