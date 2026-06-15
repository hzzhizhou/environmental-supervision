package com.neusoft.env.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("users")
public class User extends BaseEntity {

    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    private String username;

    private String password;

    private String realName;

    private String phone;

    private Integer roleId;

    private Integer gridId;

    private String avatarUrl;

    private Integer allowCrossRegion = 0;

    private Integer status = 1;

    private LocalDateTime lastLoginAt;
}