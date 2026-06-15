package com.neusoft.env.dto.response;

import lombok.Data;

@Data
public class GridWorkerRankingDTO {

    private Integer rank;

    private Integer userId;

    private String username;

    private String realName;

    private String gridName;

    private Integer completedTasks;

    private Integer totalTasks;
}