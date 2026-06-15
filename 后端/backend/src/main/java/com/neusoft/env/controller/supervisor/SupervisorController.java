package com.neusoft.env.controller.supervisor;

import com.neusoft.env.dto.response.ApiResponse;
import com.neusoft.env.dto.response.PointsDTO;
import com.neusoft.env.dto.response.RankingDTO;
import com.neusoft.env.service.supervisor.SupervisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/supervisor")
public class SupervisorController {

    @Autowired
    private SupervisorService supervisorService;

    @GetMapping("/points/{userId}")
    public ResponseEntity<ApiResponse<PointsDTO>> getPoints(@PathVariable Integer userId) {
        PointsDTO points = supervisorService.getPoints(userId);
        return ResponseEntity.ok(ApiResponse.success("获取成功", points));
    }

    @GetMapping("/points/{userId}/logs")
    public ResponseEntity<ApiResponse<List<PointsDTO.PointLog>>> getPointsLogs(@PathVariable Integer userId) {
        List<PointsDTO.PointLog> logs = supervisorService.getPointsLogs(userId);
        return ResponseEntity.ok(ApiResponse.success("获取成功", logs));
    }

    @GetMapping("/ranking")
    public ResponseEntity<ApiResponse<List<RankingDTO>>> getRanking() {
        List<RankingDTO> ranking = supervisorService.getRanking();
        return ResponseEntity.ok(ApiResponse.success("获取成功", ranking));
    }

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<String>> uploadFile(@RequestParam("file") MultipartFile file) {
        String url = supervisorService.uploadFile(file);
        return ResponseEntity.ok(ApiResponse.success("上传成功", url));
    }
}