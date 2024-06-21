package com.koh.rsa.constants;

/**
 * RSA常量
 */
public class RsaConstants {
    /**
     * RSA签名类型
     */
    public static final String SIGN_TYPE_RSA = "RSA";
    /**
     * RSA2签名类型
     */
    public static final String SIGN_TYPE_RSA2 = "RSA2";
    /**
     * RSA签名算法
     */
    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";
    /**
     * RSA2签名算法
     */
    public static final String SIGN_SHA256RSA_ALGORITHMS = "SHA256WithRSA";

    /**
     * RSA密钥长度,默认密钥长度是1024,密钥长度必须是64的倍数，在512到65536位之间,不管是RSA还是RSA2长度推荐使用2048
     */
    public static final int KEY_SIZE = 2048;
}
