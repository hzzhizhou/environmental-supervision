package com.neusoft.env.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskAssignmentDTO {

    private Integer taskId;

    private Integer reportId;

    private String reportCode;

    private Integer adminId;

    private String adminName;

    private Integer userId;

    private String userName;

    private LocalDateTime assignTime;

    private LocalDateTime deadline;

    private String status;

    private String statusDesc;

    private String remark;
}