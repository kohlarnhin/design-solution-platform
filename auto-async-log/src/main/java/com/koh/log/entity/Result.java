package com.koh.log.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @date: 2020/2/1
 * @time: 23:03
 **/
@Data
@NoArgsConstructor
public class Result<T> {
    /**
     * 操作是否成功
     */
    private boolean success;

    /**
     * 操作代码
     */
    private int code;

    /**
     * 提示信息
     */
    private String message;

    /**
     * 详细数据
     */
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private T data;

    private Result(ResultCode result) {
        this(result, null);
    }

    private Result(ResultCode result, T data) {
        this.message = result.message();
        this.code = result.code();
        this.success = result.success();
        this.data = data;
    }

    public Result(boolean success, int code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    static <Void> Result<Void> create(ResultCode result) {
        return new Result<>(result);
    }

    static <T> Result<T> create(ResultCode result, T data) {
        return new Result<T>(result, data);
    }
}
