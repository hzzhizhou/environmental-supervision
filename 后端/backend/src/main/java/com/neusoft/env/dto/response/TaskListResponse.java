package com.neusoft.env.dto.response;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

@Data
public class TaskListResponse {

    private IPage<TaskAssignmentDTO> tasks;

    private StatusStatistics statistics;

    @Data
    public static class StatusStatistics {
        private Long total;
        private Long assigned;
        private Long accepted;
        private Long finish;
    }
}