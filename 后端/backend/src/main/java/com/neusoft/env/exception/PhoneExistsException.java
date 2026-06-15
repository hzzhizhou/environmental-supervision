package com.neusoft.env.exception;

/**
 * 手机号已存在异常
 * 当用户注册时手机号已被占用抛出此异常
 */
public class PhoneExistsException extends AuthException {

    public PhoneExistsException() {
        super(409, "手机号已被注册");
    }

    public PhoneExistsException(String phone) {
        super(409, "手机号 '" + phone + "' 已被注册");
    }
}
