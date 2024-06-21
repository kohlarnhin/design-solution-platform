
package com.koh.rsa.annotation;


import com.koh.rsa.enums.RsaMode;

import java.lang.annotation.*;

/**
 * 用于添加在方法上，用于指定该方法的RSA模式
 * 优先级高于全局配置
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RsaSecure {

    /**
     * RSA模式
     * 默认是请求验签
     * @return RSA模式
     */
    RsaMode rsaMode() default RsaMode.REQUEST_ONLY;
}
