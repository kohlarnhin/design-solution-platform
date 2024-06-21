package com.koh.rsa.properties;


import com.koh.rsa.constants.RsaConstants;
import com.koh.rsa.enums.RsaMode;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * RSA配置
 */
@Data
@ConfigurationProperties(prefix = "apcp.rsa")
public class RsaProperties {

    /**
     * 是否启用 对所有接口的请求验签和返回加签
     * true 开启
     * false 关闭
     */
    private boolean enable = false;

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
     * 默认是RSA2
     */
    private String signType = RsaConstants.SIGN_TYPE_RSA2;

    /**
     * 可配置RSA默认模式
     * 默认是只对请求验签
     */
    private String defaultRsaMode = RsaMode.REQUEST_ONLY.name();
}
