package com.koh.rsa.bean;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RsaConfig {

    /**
     * 项目公钥 对方的公钥
     */
    private String publicKey;

    /**
     * 项目私钥 这个是己方的私钥
     */
    private String privateKey;

    /**
     * 签名类型
     */
    private String signType;
}
