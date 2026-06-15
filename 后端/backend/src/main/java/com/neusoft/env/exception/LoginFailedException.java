package com.neusoft.env.exception;

/**
 * 登录失败异常
 * 当用户名或密码错误时抛出此异常
 */
public class LoginFailedException extends AuthException {

    public LoginFailedException() {
        super(401, "用户名或密码错误");
    }

    public LoginFailedException(String message) {
        super(401, message);
    }
}
