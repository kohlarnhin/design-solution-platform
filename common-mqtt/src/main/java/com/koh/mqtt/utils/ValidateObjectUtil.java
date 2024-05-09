package com.koh.mqtt.utils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.util.Set;
import java.util.stream.Collectors;

import static jakarta.validation.Validation.buildDefaultValidatorFactory;


public class ValidateObjectUtil {

    public static void validate(Object object) {
        Validator validator = buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Object>> violations = validator.validate(object);
        if (!violations.isEmpty()) {
            // 校验失败，获取错误信息
            String message = violations.stream()
                    .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                    .collect(Collectors.joining(", "));
            // 抛出自定义的异常
            throw new RuntimeException(message);
        }
    }
}
