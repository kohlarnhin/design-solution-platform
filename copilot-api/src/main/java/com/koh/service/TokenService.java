package com.koh.service;


/**
 * 获取token 可能会有多个获取token的方式
 */
public interface TokenService {

    /**
     * 获取token
     * @param key 凭证
     * @return token
     */
    String getToken(String key);

}
