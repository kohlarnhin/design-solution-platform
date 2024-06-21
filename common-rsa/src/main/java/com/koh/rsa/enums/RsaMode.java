package com.koh.rsa.enums;

/**
 * RSA模式
 */
public enum RsaMode {
    /**
     * 请求验签和响应加签
     */
    BOTH,
    /**
     * 只有请求验签
     */
    REQUEST_ONLY,
    /**
     * 只有响应加签
     */
    RESPONSE_ONLY,
    /**
     * 两者都没有
     */
    NONE
}
