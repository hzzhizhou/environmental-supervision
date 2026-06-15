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
@TableName("audit_logs")
public class AuditLog {

    @TableId(value = "audit_id", type = IdType.AUTO)
    private Integer auditId;

    private Integer detectId;

    private Integer auditorId;

    private String action;

    private String result;

    private LocalDateTime auditedAt;
}