package com.neusoft.env.controller.supervisor;

import com.neusoft.env.dto.request.AqiReportCreateRequest;
import com.neusoft.env.dto.request.AqiReportQueryRequest;
import com.neusoft.env.dto.response.ApiResponse;
import com.neusoft.env.dto.response.PageResponse;
import com.neusoft.env.dto.response.AqiReportDTO;
import com.neusoft.env.entity.User;
import com.neusoft.env.service.file.FileUploadService;
import com.neusoft.env.service.supervisor.SupervisorReportService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/supervisor/reports")
public class SupervisorReportController {

    private final SupervisorReportService supervisorReportService;
    private final FileUploadService fileUploadService;

    @Autowired
    public SupervisorReportController(SupervisorReportService supervisorReportService,
                                     FileUploadService fileUploadService) {
        this.supervisorReportService = supervisorReportService;
        this.fileUploadService = fileUploadService;
    }

    private Integer getCurrentUserId() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getUserId();
    }

    @PostMapping
    public ApiResponse<AqiReportDTO> createReport(@Valid @RequestBody AqiReportCreateRequest request) {
        Integer userId = getCurrentUserId();
        request.setUserId(userId);
        AqiReportDTO report = supervisorReportService.createReport(request);
        return ApiResponse.success("上报成功", report);
    }

    @GetMapping("/{reportId}")
    public ApiResponse<AqiReportDTO> getReportById(@PathVariable Integer reportId) {
        Integer userId = getCurrentUserId();
        AqiReportDTO report = supervisorReportService.getReportById(reportId, userId);
        return ApiResponse.success(report);
    }

    @GetMapping
    public ApiResponse<PageResponse<AqiReportDTO>> getReportList(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String province,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        Integer userId = getCurrentUserId();
        
        AqiReportQueryRequest request = new AqiReportQueryRequest();
        request.setUserId(userId);
        request.setStatus(status);
        request.setProvince(province);
        request.setCity(city);
        request.setKeyword(keyword);
        request.setPageNum(pageNum);
        request.setPageSize(pageSize);

        PageResponse<AqiReportDTO> page = supervisorReportService.getReportList(request);
        return ApiResponse.success(page);
    }

    @DeleteMapping("/{reportId}")
    public ApiResponse<Void> deleteReport(@PathVariable Integer reportId) {
        Integer userId = getCurrentUserId();
        supervisorReportService.deleteReport(reportId, userId);
        return ApiResponse.success("删除成功", null);
    }

    @PostMapping("/upload-photo")
    public ApiResponse<Map<String, String>> uploadPhoto(@RequestParam("file") MultipartFile file) {
        String photoUrl = fileUploadService.uploadFile(file, "report");
        Map<String, String> result = new HashMap<>();
        result.put("url", photoUrl);
        return ApiResponse.success("图片上传成功", result);
    }
}