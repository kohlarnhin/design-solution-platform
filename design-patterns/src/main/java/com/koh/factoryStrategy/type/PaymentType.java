package com.koh.factoryStrategy.type;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum PaymentType {
    ALIPAY("alipay", "支付宝支付"),
    WECHATPAY("wxpay", "微信支付");

    private final String code;
    private final String description;

    private static final Map<String, PaymentType> codeLookup = new HashMap<>();

    static {
        for (PaymentType type : PaymentType.values()) {
            codeLookup.put(type.getCode(), type);
        }
    }

    PaymentType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static PaymentType getByCode(int code) {
        return codeLookup.get(code);
    }
}
