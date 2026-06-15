package com.neusoft.env.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 修改个人信息请求DTO
 */
@Data
public class UpdateProfileRequest {

    /**
     * 用户ID
     * 必填
     */
    @NotNull(message = "用户ID不能为空")
    private Integer userId;

    /**
     * 昵称（真实姓名）
     * 选填
     */
    private String realName;

    /**
     * 手机号
     * 选填，11位手机号格式
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
}