package com.neusoft.env.dto.request;

import lombok.Data;

@Data
public class UserUpdateRequest {

    private String realName;

    private String phone;

    private Integer roleId;

    private Integer gridId;

    private Integer status;

    private String avatarUrl;
}