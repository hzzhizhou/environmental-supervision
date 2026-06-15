package com.neusoft.env.service.gridWorker;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.neusoft.env.dto.request.DetectDataCreateRequest;
import com.neusoft.env.dto.response.DetectDataDTO;
import com.neusoft.env.dto.response.RankingResponse;
import com.neusoft.env.dto.response.TaskAssignmentDTO;
import com.neusoft.env.dto.response.TaskListResponse;

public interface GridWorkerService {

    TaskListResponse getAssignedTasksWithStatistics(Integer userId, String status, Integer pageNum, Integer pageSize);

    RankingResponse getRanking(Integer userId, Integer pageNum, Integer pageSize);

    TaskAssignmentDTO getTaskById(Integer taskId);

    void acceptTask(Integer taskId, Integer userId);

    DetectDataDTO submitDetectData(DetectDataCreateRequest request);

    IPage<DetectDataDTO> getMyDetectData(Integer userId, Integer status, Integer pageNum, Integer pageSize);

    DetectDataDTO getDetectDataById(Integer detectId);
}