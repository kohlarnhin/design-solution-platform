package com.koh.rsa.util;


import com.koh.rsa.bean.RsaConfig;
import com.koh.rsa.constants.RsaConstants;
import com.koh.rsa.provider.RsaExceptionProvider;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RSA工具类，用于生成密钥对、签名、校验签名
 * 提供默认RUNTIME异常和自定义异常两种异常处理方式
 */
public class RsaSignatureUtil {

    private static final RsaExceptionProvider rsaExceptionProvider = RsaExceptionProvider.defaultProvider();

    /**
     * 生成密钥对
     *
     * @return 返回包含公私钥的map
     */
    public static RsaConfig generateKey() {
        KeyPairGenerator keygen;
        try {
            keygen = KeyPairGenerator.getInstance(RsaConstants.SIGN_TYPE_RSA);
        } catch (NoSuchAlgorithmException e) {
            throw rsaExceptionProvider.checkRsaParamException("RSA初始化密钥出现错误,算法异常", e);
        }
        SecureRandom secrand = new SecureRandom();
        //初始化随机产生器
        secrand.setSeed("apcp".getBytes());
        //初始化密钥生成器
        keygen.initialize(RsaConstants.KEY_SIZE, secrand);
        KeyPair keyPair = keygen.genKeyPair();
        //获取公钥并转成base64编码
        byte[] pubKey = keyPair.getPublic().getEncoded();
        String publicKey = Base64.getEncoder().encodeToString(pubKey);
        //获取私钥并转成base64编码
        byte[] priKey = keyPair.getPrivate().getEncoded();
        String privateKey = Base64.getEncoder().encodeToString(priKey);
        return new RsaConfig(publicKey, privateKey, RsaConstants.SIGN_TYPE_RSA);
    }

    /**
     * 生成密钥对
     *
     * @return 返回包含公私钥的map
     */
    public static RsaConfig generateKey(RsaExceptionProvider rsaExceptionProvider) {
        KeyPairGenerator keygen;
        try {
            keygen = KeyPairGenerator.getInstance(RsaConstants.SIGN_TYPE_RSA);
        } catch (NoSuchAlgorithmException e) {
            throw rsaExceptionProvider.checkRsaParamException("RSA初始化密钥出现错误,算法异常", e);
        }
        SecureRandom secrand = new SecureRandom();
        //初始化随机产生器
        secrand.setSeed("apcp".getBytes());
        //初始化密钥生成器
        keygen.initialize(RsaConstants.KEY_SIZE, secrand);
        KeyPair keyPair = keygen.genKeyPair();
        //获取公钥并转成base64编码
        byte[] pubKey = keyPair.getPublic().getEncoded();
        String publicKey = Base64.getEncoder().encodeToString(pubKey);
        //获取私钥并转成base64编码
        byte[] priKey = keyPair.getPrivate().getEncoded();
        String privateKey = Base64.getEncoder().encodeToString(priKey);
        return new RsaConfig(publicKey, privateKey, RsaConstants.SIGN_TYPE_RSA);
    }

    /**
     * RSA签名
     *
     * @param content    待签名数据
     * @param privateKey 私钥
     * @param signType   RSA或RSA2
     * @return 签名
     * @throws RuntimeException RSA异常
     */
    public static String sign(String content, String privateKey, String signType) {
        try {
            //创建PKCS8编码密钥规范
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));
            //返回转换指定算法的KeyFactory对象
            KeyFactory keyFactory = KeyFactory.getInstance(RsaConstants.SIGN_TYPE_RSA);
            //根据PKCS8编码密钥规范产生私钥对象
            PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
            //标准签名算法名称(RSA还是RSA2)
            String algorithm = RsaConstants.SIGN_TYPE_RSA.equals(signType) ? RsaConstants.SIGN_ALGORITHMS : RsaConstants.SIGN_SHA256RSA_ALGORITHMS;
            //用指定算法产生签名对象Signature
            Signature signature = Signature.getInstance(algorithm);
            //用私钥初始化签名对象Signature
            signature.initSign(priKey);
            //将待签名的数据传送给签名对象(须在初始化之后)
            signature.update(content.getBytes());
            //返回签名结果字节数组
            byte[] sign = signature.sign();
            //返回Base64编码后的字符串
            return Base64.getEncoder().encodeToString(sign);
        } catch (InvalidKeySpecException e) {
            throw rsaExceptionProvider.checkRsaParamException("RSA私钥格式不正确，请检查是否正确配置了PKCS8格式的私钥", e);
        } catch (Exception e) {
            throw rsaExceptionProvider.rsaSignException("RSA签名异常", e);
        }
    }

    /**
     * RSA签名
     *
     * @param content    待签名数据
     * @param privateKey 私钥
     * @param signType   RSA或RSA2
     * @return 签名
     * @throws RuntimeException RSA异常
     */
    public static String sign(String content, String privateKey, String signType, RsaExceptionProvider rsaExceptionProvider) {
        try {
            //创建PKCS8编码密钥规范
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));
            //返回转换指定算法的KeyFactory对象
            KeyFactory keyFactory = KeyFactory.getInstance(RsaConstants.SIGN_TYPE_RSA);
            //根据PKCS8编码密钥规范产生私钥对象
            PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
            //标准签名算法名称(RSA还是RSA2)
            String algorithm = RsaConstants.SIGN_TYPE_RSA.equals(signType) ? RsaConstants.SIGN_ALGORITHMS : RsaConstants.SIGN_SHA256RSA_ALGORITHMS;
            //用指定算法产生签名对象Signature
            Signature signature = Signature.getInstance(algorithm);
            //用私钥初始化签名对象Signature
            signature.initSign(priKey);
            //将待签名的数据传送给签名对象(须在初始化之后)
            signature.update(content.getBytes());
            //返回签名结果字节数组
            byte[] sign = signature.sign();
            //返回Base64编码后的字符串
            return Base64.getEncoder().encodeToString(sign);
        } catch (InvalidKeySpecException e) {
            throw rsaExceptionProvider.checkRsaParamException("RSA私钥格式不正确，请检查是否正确配置了PKCS8格式的私钥", e);
        } catch (Exception e) {
            throw rsaExceptionProvider.rsaSignException("RSA签名异常", e);
        }
    }

    /**
     * RSA校验数字签名
     *
     * @param content   待校验数据
     * @param sign      数字签名
     * @param publicKey 公钥
     * @param signType  RSA或RSA2
     * @return boolean 校验成功返回true，失败返回false
     */
    public static boolean verify(String content, String sign, String publicKey, String signType) {
        try {
            //返回转换指定算法的KeyFactory对象
            KeyFactory keyFactory = KeyFactory.getInstance(RsaConstants.SIGN_TYPE_RSA);
            //创建X509编码密钥规范
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey));
            //根据X509编码密钥规范产生公钥对象
            PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
            //标准签名算法名称(RSA还是RSA2)
            String algorithm = RsaConstants.SIGN_TYPE_RSA.equals(signType) ? RsaConstants.SIGN_ALGORITHMS : RsaConstants.SIGN_SHA256RSA_ALGORITHMS;
            //用指定算法产生签名对象Signature
            Signature signature = Signature.getInstance(algorithm);
            //用公钥初始化签名对象,用于验证签名
            signature.initVerify(pubKey);
            //更新签名内容
            signature.update(content.getBytes());
            //得到验证结果
            return signature.verify(Base64.getDecoder().decode(sign));
        } catch (Exception e) {
            throw rsaExceptionProvider.rsaSignException(
                    "验签失败，RSA content = " + content + ",sign=" + sign, e);
        }
    }

    /**
     * RSA校验数字签名
     *
     * @param content   待校验数据
     * @param sign      数字签名
     * @param publicKey 公钥
     * @param signType  RSA或RSA2
     * @return boolean 校验成功返回true，失败返回false
     */
    public static boolean verify(String content, String sign, String publicKey, String signType, RsaExceptionProvider rsaExceptionProvider) {
        try {
            //返回转换指定算法的KeyFactory对象
            KeyFactory keyFactory = KeyFactory.getInstance(RsaConstants.SIGN_TYPE_RSA);
            //创建X509编码密钥规范
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey));
            //根据X509编码密钥规范产生公钥对象
            PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
            //标准签名算法名称(RSA还是RSA2)
            String algorithm = RsaConstants.SIGN_TYPE_RSA.equals(signType) ? RsaConstants.SIGN_ALGORITHMS : RsaConstants.SIGN_SHA256RSA_ALGORITHMS;
            //用指定算法产生签名对象Signature
            Signature signature = Signature.getInstance(algorithm);
            //用公钥初始化签名对象,用于验证签名
            signature.initVerify(pubKey);
            //更新签名内容
            signature.update(content.getBytes());
            //得到验证结果
            return signature.verify(Base64.getDecoder().decode(sign));
        } catch (Exception e) {
            throw rsaExceptionProvider.rsaSignException(
                    "验签失败，RSA content = " + content + ",sign=" + sign, e);
        }
    }
}
