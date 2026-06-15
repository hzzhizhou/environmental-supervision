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
@TableName("task_assignments")
public class TaskAssignment {

    @TableId(value = "task_id", type = IdType.AUTO)
    private Integer taskId;

    private String taskCode;

    private Integer reportId;

    private Integer adminId;

    private Integer userId;

    private String status = "assigned";

    private String remark;

    private LocalDateTime assignedAt;

    private LocalDateTime completedAt;
}