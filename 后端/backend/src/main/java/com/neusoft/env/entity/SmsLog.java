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
@TableName("sms_logs")
public class SmsLog {

    @TableId(value = "sms_id", type = IdType.AUTO)
    private Integer smsId;

    private String phone;

    private String code;

    private String type;

    private Integer isUsed = 0;

    private LocalDateTime expiredAt;
}