package com.neusoft.env.exception;

/**
 * 用户不存在异常
 * 当查找用户不存在时抛出此异常
 */
public class UserNotFoundException extends AuthException {

    public UserNotFoundException() {
        super(404, "用户不存在");
    }

    public UserNotFoundException(Integer userId) {
        super(404, "用户ID '" + userId + "' 不存在");
    }

    public UserNotFoundException(String username) {
        super(404, "用户 '" + username + "' 不存在");
    }
}
