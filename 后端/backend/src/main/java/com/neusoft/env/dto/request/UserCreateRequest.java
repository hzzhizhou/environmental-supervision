package com.neusoft.env.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserCreateRequest {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    private String realName;

    @NotBlank(message = "手机号不能为空")
    private String phone;

    @NotNull(message = "角色ID不能为空")
    private Integer roleId;

    private Integer gridId;

    private Integer status = 1;
}