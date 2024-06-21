package com.koh.rsa.service;


import com.koh.rsa.bean.RsaConfig;
import com.koh.rsa.properties.RsaProperties;
import com.koh.rsa.provider.RsaConfigProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

/**
 * 可直接查询当前系统的RSA配置
 */
@Component
public class RsaService {

    @Autowired(required = false)
    private RsaConfigProvider rsaConfigProvider;
    @Autowired
    private RsaProperties rsaProperties;

    /**
     * 获取公钥 默认方式
     *
     * @return 返回公钥
     */
    public String getPublicKey() {
        return Optional.ofNullable(rsaConfigProvider)
                .map(RsaConfigProvider::getRsaConfig)
                .map(RsaConfig::getPublicKey)
                .filter(content -> !ObjectUtils.isEmpty(content))
                .orElse(rsaProperties.getPublicKey());
    }

    /**
     * 获取私钥 默认方式
     *
     * @return 返回私钥
     */
    public String getPrivateKey() {
        return (Optional.ofNullable(rsaConfigProvider)
                .map(RsaConfigProvider::getRsaConfig)
                .map(RsaConfig::getPrivateKey)
                .filter(content -> !ObjectUtils.isEmpty(content))
                .orElse(rsaProperties.getPrivateKey()));
    }

    /**
     * 获取签名类型 默认方式
     *
     * @return 返回签名类型
     */
    public String getSignType() {
        return Optional.ofNullable(rsaConfigProvider)
                .map(RsaConfigProvider::getRsaConfig)
                .map(RsaConfig::getSignType)
                .filter(content -> !ObjectUtils.isEmpty(content))
                .orElse(rsaProperties.getSignType());
    }
}
