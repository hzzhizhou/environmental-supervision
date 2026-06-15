package com.neusoft.env.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 用户登录请求DTO
 * 用于接收用户登录时提交的参数
 */
@Data
public class LoginRequest {

    /**
     * 用户名
     * 必填，非空
     * 注册时填写的登录账号
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     * 必填，非空
     * 注册时设置的密码（明文传递，后端会进行BCrypt加密验证）
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 角色ID
     * 选填，用于角色判断
     * 如果填写，则验证用户是否属于该角色
     * 可选值：1-管理员，2-网格员，3-公众监督员，4-决策者
     */
    private Integer roleId;
}
