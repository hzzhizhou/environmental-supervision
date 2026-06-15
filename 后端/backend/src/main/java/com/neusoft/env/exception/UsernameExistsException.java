package com.neusoft.env.exception;

/**
 * 用户名已存在异常
 * 当用户注册时用户名已被占用抛出此异常
 */
public class UsernameExistsException extends AuthException {

    public UsernameExistsException() {
        super(409, "用户名已存在");
    }

    public UsernameExistsException(String username) {
        super(409, "用户名 '" + username + "' 已被注册");
    }
}
