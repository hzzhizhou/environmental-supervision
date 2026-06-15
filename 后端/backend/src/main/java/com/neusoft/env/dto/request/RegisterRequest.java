package com.neusoft.env.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 用户注册请求DTO
 * 用于接收用户注册时提交的参数
 */
@Data
public class RegisterRequest {

    /**
     * 用户名
     * 必填，非空
     * 长度限制：3-20个字符
     * 用于登录的账号，需唯一
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度必须在3-20之间")
    private String username;

    /**
     * 密码
     * 必填，非空
     * 长度限制：6-50个字符
     * 明文传递，后端会进行BCrypt加密后存储
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 50, message = "密码长度必须在6-50之间")
    private String password;

    /**
     * 真实姓名
     * 必填，非空
     * 用户的真实姓名，用于显示
     */
    @NotBlank(message = "真实姓名不能为空")
    private String realName;

    /**
     * 手机号
     * 必填，非空
     * 长度限制：必须为11位数字
     * 需唯一，用于接收验证码等
     */
    @NotBlank(message = "手机号不能为空")
    @Size(min = 11, max = 11, message = "手机号必须为11位")
    private String phone;

    /**
     * 角色ID
     * 选填，默认值：3（公众监督员）
     */
    private Integer roleId = 3;

    /**
     * 网格ID
     * 选填
     * 当角色为网格员（roleId=2）时，建议填写所属网格
     * 关联grids表的grid_id
     */
    private Integer gridId;

    /**
     * 验证码
     * 必填，写死为：123456
     */
    @NotBlank(message = "验证码不能为空")
    private String code;
}
