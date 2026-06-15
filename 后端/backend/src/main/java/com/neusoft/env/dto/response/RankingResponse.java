package com.neusoft.env.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class RankingResponse {

    private List<GridWorkerRankingDTO> records;

    private Long total;

    private Integer size;

    private Integer current;

    private Integer pages;

    private Integer myRank;

    private Integer myCompletedTasks;
}