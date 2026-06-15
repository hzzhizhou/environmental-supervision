package com.neusoft.env.service.gridWorker.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.env.dto.request.DetectDataCreateRequest;
import com.neusoft.env.dto.response.DetectDataDTO;
import com.neusoft.env.dto.response.GridWorkerRankingDTO;
import com.neusoft.env.dto.response.RankingResponse;
import com.neusoft.env.dto.response.TaskAssignmentDTO;
import com.neusoft.env.dto.response.TaskListResponse;
import com.neusoft.env.entity.AqiReport;
import com.neusoft.env.entity.DetectData;
import com.neusoft.env.entity.Grid;
import com.neusoft.env.entity.TaskAssignment;
import com.neusoft.env.entity.User;
import com.neusoft.env.mapper.AqiReportMapper;
import com.neusoft.env.mapper.DetectDataMapper;
import com.neusoft.env.mapper.GridMapper;
import com.neusoft.env.mapper.TaskAssignmentMapper;
import com.neusoft.env.mapper.UserMapper;
import com.neusoft.env.service.gridWorker.GridWorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GridWorkerServiceImpl implements GridWorkerService {

    @Autowired
    private TaskAssignmentMapper taskAssignmentMapper;

    @Autowired
    private DetectDataMapper detectDataMapper;

    @Autowired
    private AqiReportMapper reportMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private GridMapper gridMapper;

    @Override
    public TaskListResponse getAssignedTasksWithStatistics(Integer userId, String status, Integer pageNum, Integer pageSize) {
        TaskListResponse response = new TaskListResponse();
        
        LambdaQueryWrapper<TaskAssignment> countQuery = new LambdaQueryWrapper<>();
        countQuery.eq(TaskAssignment::getUserId, userId);
        Long total = taskAssignmentMapper.selectCount(countQuery);
        
        countQuery.clear();
        countQuery.eq(TaskAssignment::getUserId, userId);
        countQuery.eq(TaskAssignment::getStatus, "assigned");
        Long assigned = taskAssignmentMapper.selectCount(countQuery);
        
        countQuery.clear();
        countQuery.eq(TaskAssignment::getUserId, userId);
        countQuery.eq(TaskAssignment::getStatus, "accepted");
        Long accepted = taskAssignmentMapper.selectCount(countQuery);
        
        countQuery.clear();
        countQuery.eq(TaskAssignment::getUserId, userId);
        countQuery.eq(TaskAssignment::getStatus, "completed");
        Long finish = taskAssignmentMapper.selectCount(countQuery);
        
        TaskListResponse.StatusStatistics statistics = new TaskListResponse.StatusStatistics();
        statistics.setTotal(total);
        statistics.setAssigned(assigned);
        statistics.setAccepted(accepted);
        statistics.setFinish(finish);
        response.setStatistics(statistics);
        
        Page<TaskAssignment> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<TaskAssignment> query = new LambdaQueryWrapper<>();
        query.eq(TaskAssignment::getUserId, userId);
        
        if (status != null && !status.isEmpty()) {
            query.eq(TaskAssignment::getStatus, status);
        }
        query.orderByDesc(TaskAssignment::getAssignedAt);
        
        IPage<TaskAssignment> taskPage = taskAssignmentMapper.selectPage(page, query);
        
        Page<TaskAssignmentDTO> resultPage = new Page<>(taskPage.getCurrent(), taskPage.getSize(), taskPage.getTotal());
        resultPage.setRecords(taskPage.getRecords().stream().map(task -> {
            User user = userMapper.selectById(task.getUserId());
            AqiReport report = reportMapper.selectById(task.getReportId());
            return convertToTaskDTO(task, user != null ? user.getRealName() : null,
                    report != null ? report.getReportCode() : null);
        }).collect(Collectors.toList()));
        response.setTasks(resultPage);
        
        return response;
    }

    @Override
    public RankingResponse getRanking(Integer userId, Integer pageNum, Integer pageSize) {
        RankingResponse response = new RankingResponse();

        LambdaQueryWrapper<TaskAssignment> query = new LambdaQueryWrapper<>();
        query.isNotNull(TaskAssignment::getUserId);

        List<TaskAssignment> tasks = taskAssignmentMapper.selectList(query);
        
        java.util.Map<Integer, Integer> completedCounts = new java.util.HashMap<>();
        java.util.Map<Integer, Integer> totalCounts = new java.util.HashMap<>();
        
        for (TaskAssignment task : tasks) {
            totalCounts.merge(task.getUserId(), 1, Integer::sum);
            if ("completed".equals(task.getStatus())) {
                completedCounts.merge(task.getUserId(), 1, Integer::sum);
            }
        }

        java.util.List<GridWorkerRankingDTO> allRankings = new java.util.ArrayList<>();
        for (Integer uid : totalCounts.keySet()) {
            User user = userMapper.selectById(uid);
            if (user != null && user.getRoleId() == 2) {
                GridWorkerRankingDTO dto = new GridWorkerRankingDTO();
                dto.setUserId(uid);
                dto.setUsername(user.getUsername());
                dto.setRealName(user.getRealName());
                dto.setTotalTasks(totalCounts.get(uid));
                dto.setCompletedTasks(completedCounts.getOrDefault(uid, 0));
                
                if (user.getGridId() != null) {
                    Grid grid = gridMapper.selectById(user.getGridId());
                    if (grid != null) {
                        dto.setGridName(grid.getGridName());
                    }
                }
                allRankings.add(dto);
            }
        }

        allRankings.sort((a, b) -> Integer.compare(b.getCompletedTasks(), a.getCompletedTasks()));

        for (int i = 0; i < allRankings.size(); i++) {
            allRankings.get(i).setRank(i + 1);
        }

        int start = (pageNum - 1) * pageSize;
        int end = Math.min(start + pageSize, allRankings.size());
        java.util.List<GridWorkerRankingDTO> pageRecords = start < allRankings.size() ? allRankings.subList(start, end) : new java.util.ArrayList<>();

        response.setRecords(pageRecords);
        response.setTotal((long) allRankings.size());
        response.setSize(pageSize);
        response.setCurrent(pageNum);
        response.setPages((int) Math.ceil((double) allRankings.size() / pageSize));

        for (GridWorkerRankingDTO ranking : allRankings) {
            if (ranking.getUserId().equals(userId)) {
                response.setMyRank(ranking.getRank());
                response.setMyCompletedTasks(ranking.getCompletedTasks());
                break;
            }
        }

        return response;
    }

    @Override
    public TaskAssignmentDTO getTaskById(Integer taskId) {
        TaskAssignment task = taskAssignmentMapper.selectById(taskId);
        if (task == null) {
            throw new RuntimeException("任务不存在");
        }

        User user = userMapper.selectById(task.getUserId());
        AqiReport report = reportMapper.selectById(task.getReportId());

        return convertToTaskDTO(task, user != null ? user.getRealName() : null,
                report != null ? report.getReportCode() : null);
    }

    @Override
    @Transactional
    public void acceptTask(Integer taskId, Integer userId) {
        TaskAssignment task = taskAssignmentMapper.selectById(taskId);
        if (task == null) {
            throw new RuntimeException("任务不存在");
        }

        if (!task.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此任务");
        }

        if (!"assigned".equals(task.getStatus())) {
            throw new RuntimeException("任务状态不允许接受");
        }

        task.setStatus("accepted");
        taskAssignmentMapper.updateById(task);
    }

    

    @Override
    @Transactional
    public DetectDataDTO submitDetectData(DetectDataCreateRequest request) {
        AqiReport report = reportMapper.selectById(request.getReportId());
        if (report == null) {
            throw new RuntimeException("上报记录不存在");
        }

        TaskAssignment task = taskAssignmentMapper.selectOne(
            new LambdaQueryWrapper<TaskAssignment>()
                .eq(TaskAssignment::getReportId, request.getReportId())
                .eq(TaskAssignment::getUserId, request.getUserId())
        );
        if (task == null) {
            throw new RuntimeException("无权提交此上报的检测数据");
        }

        if (!"accepted".equals(task.getStatus())) {
            throw new RuntimeException("任务状态不允许提交检测数据");
        }

        DetectData detectData = new DetectData();
        detectData.setTaskId(task.getTaskId());
        detectData.setUserId(request.getUserId());
        detectData.setAqiValue(request.getAqiValue());
        detectData.setAqiLevel(request.getAqiLevel());
        detectData.setPm25(request.getPm25());
        detectData.setPm10(request.getPm10());
        detectData.setO3(request.getO3());
        detectData.setSo2(request.getSo2());
        detectData.setNo2(request.getNo2());
        detectData.setCo(request.getCo());
        detectData.setLocation(request.getLocation());
        detectData.setStatus(0);
        detectData.setDetectTime(LocalDateTime.now());

        detectDataMapper.insert(detectData);

        task.setStatus("completed");
        taskAssignmentMapper.updateById(task);

        report.setStatus("detected");
        reportMapper.updateById(report);

        return convertToDetectDTO(detectData);
    }

    @Override
    public IPage<DetectDataDTO> getMyDetectData(Integer userId, Integer status, Integer pageNum, Integer pageSize) {
        Page<DetectData> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<DetectData> query = new LambdaQueryWrapper<>();
        query.eq(DetectData::getUserId, userId);
        
        if (status != null) {
            query.eq(DetectData::getStatus, status);
        }
        query.orderByDesc(DetectData::getDetectTime);
        
        IPage<DetectData> detectPage = detectDataMapper.selectPage(page, query);
        Page<DetectDataDTO> resultPage = new Page<>(detectPage.getCurrent(), detectPage.getSize(), detectPage.getTotal());
        resultPage.setRecords(detectPage.getRecords().stream().map(this::convertToDetectDTO).collect(Collectors.toList()));
        return resultPage;
    }

    @Override
    public DetectDataDTO getDetectDataById(Integer detectId) {
        DetectData detectData = detectDataMapper.selectById(detectId);
        if (detectData == null) {
            throw new RuntimeException("检测数据不存在");
        }
        return convertToDetectDTO(detectData);
    }

    private TaskAssignmentDTO convertToTaskDTO(TaskAssignment task, String userName, String reportCode) {
        TaskAssignmentDTO dto = new TaskAssignmentDTO();
        dto.setTaskId(task.getTaskId());
        dto.setReportId(task.getReportId());
        dto.setReportCode(reportCode);
        dto.setUserId(task.getUserId());
        dto.setUserName(userName);
        dto.setAssignTime(task.getAssignedAt());
        dto.setDeadline(task.getCompletedAt());
        dto.setStatus(task.getStatus());
        dto.setStatusDesc(getTaskStatusDesc(task.getStatus()));
        dto.setRemark(task.getRemark());
        return dto;
    }

    private DetectDataDTO convertToDetectDTO(DetectData detect) {
        DetectDataDTO dto = new DetectDataDTO();
        dto.setDetectId(detect.getDetectId());
        dto.setTaskId(detect.getTaskId());
        dto.setUserId(detect.getUserId());
        dto.setDetectTime(detect.getDetectTime());
        dto.setAqiValue(detect.getAqiValue());
        dto.setAqiLevel(detect.getAqiLevel());
        dto.setPm25(detect.getPm25());
        dto.setPm10(detect.getPm10());
        dto.setO3(detect.getO3());
        dto.setSo2(detect.getSo2());
        dto.setNo2(detect.getNo2());
        dto.setCo(detect.getCo());
        dto.setLocation(detect.getLocation());
        dto.setPhotos(detect.getPhotos());
        dto.setRemark(detect.getRemark());
        dto.setStatus(detect.getStatus());
        dto.setStatusDesc(getStatusDesc(detect.getStatus()));
        dto.setConfirmNote(detect.getConfirmNote());
        dto.setConfirmedAt(detect.getConfirmedAt());

        TaskAssignment task = taskAssignmentMapper.selectById(detect.getTaskId());
        if (task != null) {
            AqiReport report = reportMapper.selectById(task.getReportId());
            if (report != null) {
                dto.setReportCode(report.getReportCode());
            }
        }

        User user = userMapper.selectById(detect.getUserId());
        if (user != null) {
            dto.setUserName(user.getRealName());
        }

        return dto;
    }

    private String getTaskStatusDesc(String status) {
        return switch (status) {
            case "assigned" -> "已指派";
            case "accepted" -> "已接受";
            case "completed" -> "已完成";
            default -> status;
        };
    }

    private String getStatusDesc(Integer status) {
        return switch (status) {
            case 0 -> "待审核";
            case 1 -> "合理";
            case 2 -> "不合理";
            default -> "未知";
        };
    }

    private String calculateAqiLevel(Integer aqiValue) {
        if (aqiValue <= 50) return "优";
        if (aqiValue <= 100) return "良";
        if (aqiValue <= 150) return "轻度污染";
        if (aqiValue <= 200) return "中度污染";
        if (aqiValue <= 300) return "重度污染";
        return "严重污染";
    }
}