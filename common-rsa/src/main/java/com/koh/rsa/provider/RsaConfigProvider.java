package com.koh.rsa.provider;


import com.koh.rsa.bean.RsaConfig;

/**
 * 如果调用者需要自定义获取Rsa配置的方式，可以实现该接口
 * 接口返回的RsaConfig对象中的属性值如果为空，则会使用默认配置的值，如果默认配置也为空，则会抛出异常
 */
public interface RsaConfigProvider {
    /**
     * 获取Rsa需要的配置
     *
     * @return 签名类型
     */
    RsaConfig getRsaConfig();

    /**
     * 如果需要自定义Rsa签名内容，可以实现该方法
     * 返回不为空的函数，会覆盖默认的Rsa签名内容
     *
     * @return Rsa签名内容的供应函数
     */
    String getRsaContent();

    /**
     * 如果需要自定义获取Rsa签名内容的方式，可以实现该方法
     * 返回不为空的字符串，会覆盖默认的Rsa签名内容
     *
     * @return Rsa签名内容的供应函数
     */
    String getSign();

    /**
     * 如果需要修改返回内容，可以实现该方法
     *
     * @return Rsa签名内容
     */
    Object getRsaResult(Object result);
}
