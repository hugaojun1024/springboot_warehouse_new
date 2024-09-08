package com.zds.springboot.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 接口返回数据的统一包装类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private String code;
    private String msg;
    private Object data;

    public static Result success() {
        return new Result(Constants.CODE_SUCCESS, "", null);
    }

    public static Result success(Object data) {
        return new Result(Constants.CODE_SUCCESS, "", data);
    }

    public static Result success(String msg, Object data) {
        return new Result(Constants.CODE_SUCCESS, msg, data);
    }

    public static Result error() {
        return new Result(Constants.CODE_WRONG_SYSTEM, "", null);
    }

    public static Result error(String code) {
        return new Result(code, "", null);
    }

    public static Result error(String code, String msg) {
        return new Result(code, msg, null);
    }

    public static Result error(String code, String msg, Object data) {
        return new Result(code, msg, data);
    }

}
