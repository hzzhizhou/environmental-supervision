package com.neusoft.env.controller.gridWorker;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.neusoft.env.dto.request.DetectDataCreateRequest;
import com.neusoft.env.dto.response.ApiResponse;
import com.neusoft.env.dto.response.DetectDataDTO;
import com.neusoft.env.dto.response.RankingResponse;
import com.neusoft.env.dto.response.TaskAssignmentDTO;
import com.neusoft.env.dto.response.TaskListResponse;
import com.neusoft.env.service.gridWorker.GridWorkerService;
import com.neusoft.env.config.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/grid-worker")
public class GridWorkerController {

    @Autowired
    private GridWorkerService gridWorkerService;

    @Autowired
    private JwtUtil jwtUtil;

    private Integer getCurrentUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            return jwtUtil.getUserIdFromToken(token);
        }
        throw new RuntimeException("用户未登录");
    }

    @GetMapping("/tasks")
    public ResponseEntity<ApiResponse<TaskListResponse>> getAssignedTasks(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            HttpServletRequest request) {
        
        Integer userId = getCurrentUserId(request);
        TaskListResponse response = gridWorkerService.getAssignedTasksWithStatistics(userId, status, pageNum, pageSize);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<ApiResponse<TaskAssignmentDTO>> getTaskById(@PathVariable Integer taskId) {
        TaskAssignmentDTO task = gridWorkerService.getTaskById(taskId);
        return ResponseEntity.ok(ApiResponse.success(task));
    }

    @PutMapping("/tasks/{taskId}/accept")
    public ResponseEntity<ApiResponse<Void>> acceptTask(
            @PathVariable Integer taskId,
            HttpServletRequest request) {
        Integer userId = getCurrentUserId(request);
        gridWorkerService.acceptTask(taskId, userId);
        return ResponseEntity.ok(ApiResponse.success("任务已接受", null));
    }

    @GetMapping("/ranking")
    public ResponseEntity<ApiResponse<RankingResponse>> getRanking(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            HttpServletRequest request) {
        
        Integer userId = getCurrentUserId(request);
        RankingResponse response = gridWorkerService.getRanking(userId, pageNum, pageSize);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/detect-data")
    public ResponseEntity<ApiResponse<DetectDataDTO>> submitDetectData(@Valid @RequestBody DetectDataCreateRequest request, HttpServletRequest httpRequest) {
        Integer userId = getCurrentUserId(httpRequest);
        request.setUserId(userId);
        DetectDataDTO detectData = gridWorkerService.submitDetectData(request);
        return ResponseEntity.ok(ApiResponse.success("检测数据提交成功", detectData));
    }

    @GetMapping("/detect-data")
    public ResponseEntity<ApiResponse<IPage<DetectDataDTO>>> getMyDetectData(
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            HttpServletRequest request) {
        
        Integer userId = getCurrentUserId(request);
        IPage<DetectDataDTO> data = gridWorkerService.getMyDetectData(userId, status, pageNum, pageSize);
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    @GetMapping("/detect-data/{detectId}")
    public ResponseEntity<ApiResponse<DetectDataDTO>> getDetectDataById(@PathVariable Integer detectId) {
        DetectDataDTO detectData = gridWorkerService.getDetectDataById(detectId);
        return ResponseEntity.ok(ApiResponse.success(detectData));
    }
}