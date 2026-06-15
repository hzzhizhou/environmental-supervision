package com.neusoft.env.exception;

/**
 * 认证异常基类
 * 用于处理登录注册相关的异常
 */
public class AuthException extends RuntimeException {

    private final int code;

    public AuthException(String message) {
        super(message);
        this.code = 401;
    }

    public AuthException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
