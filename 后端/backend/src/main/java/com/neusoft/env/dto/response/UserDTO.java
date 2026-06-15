package com.neusoft.env.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDTO {

    private Integer userId;

    private String username;

    private String realName;

    private String phone;

    private Integer roleId;

    private String roleName;

    private Integer gridId;

    private String gridName;

    private String avatarUrl;

    private Integer status;

    private String statusDesc;

    private LocalDateTime lastLoginAt;

    private LocalDateTime createdAt;
}