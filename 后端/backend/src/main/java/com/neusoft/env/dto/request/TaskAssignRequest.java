package com.neusoft.env.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskAssignRequest {

    @NotNull(message = "上报ID不能为空")
    private Integer reportId;

    @NotNull(message = "管理员ID不能为空")
    private Integer adminId;

    @NotNull(message = "网格员ID不能为空")
    private Integer userId;

    private LocalDateTime deadline;
}