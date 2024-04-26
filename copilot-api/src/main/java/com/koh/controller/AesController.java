package com.koh.controller;

import com.koh.properties.CopilotProperties;
import com.koh.utils.AesUtils;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * AES加密控制层
 */
@Slf4j
@RestController
public class AesController {

    @Autowired
    private AesUtils aesUtils;
    @Autowired
    private CopilotProperties copilotProperties;

    @GetMapping("/aesToken")
    public String aesToken(@RequestParam(value = "ghu", required = false) String ghu,
                           @RequestParam(value = "key", required = false) String key) {
        try {
            if (StringUtils.isNotEmpty(ghu) && StringUtils.isNotEmpty(key)) {
                String aes = aesUtils.aesEncryptForFront(ghu, key);
                log.info("\n ghu:{}\n key:{}\n 加密后:{}", ghu, key, aes);
            } else {
                //将从配置中获取
                Assert.isTrue(!copilotProperties.getGhu().isBlank(), "ghu为空需要配置");
                Assert.isTrue(!copilotProperties.getKey().isBlank(), "key为空需要配置");
                String aes = aesUtils.aesEncryptForFront(copilotProperties.getGhu(), copilotProperties.getKey());
                log.info("\n ghu:{}\n key:{}\n 加密后:{}", copilotProperties.getGhu(), copilotProperties.getKey(), aes);
            }
            return "加密成功，请查看日志";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
