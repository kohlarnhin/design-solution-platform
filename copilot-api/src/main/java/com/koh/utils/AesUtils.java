package com.koh.utils;

import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class AesUtils {

    /**
     * AES加密文本
     *
     * @param content    明文
     * @param encryptKey 秘钥，必须为16个字符组成
     * @return 密文
     */
    public String aesEncryptForFront(String content, String encryptKey) {
        if (StringUtils.isEmpty(content) || StringUtils.isEmpty(encryptKey)) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), "AES"));

            byte[] encryptStr = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptStr);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * AES解密文本
     *
     * @param encryptStr 密文
     * @param decryptKey 秘钥，必须为16个字符组成
     * @return 明文
     */
    public String aesDecryptForFront(String encryptStr, String decryptKey) {
        if (StringUtils.isEmpty(encryptStr) || StringUtils.isEmpty(decryptKey)) {
            return null;
        }
        try {
            byte[] encryptByte = Base64.getDecoder().decode(encryptStr);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));
            byte[] decryptBytes = cipher.doFinal(encryptByte);
            return new String(decryptBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
