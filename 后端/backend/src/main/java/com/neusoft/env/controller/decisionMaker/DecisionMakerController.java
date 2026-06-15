package com.neusoft.env.controller.decisionMaker;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.neusoft.env.dto.response.ApiResponse;
import com.neusoft.env.dto.response.DashboardDTO;
import com.neusoft.env.dto.response.ReportDetailDTO;
import com.neusoft.env.dto.response.StatisticsDTO;
import com.neusoft.env.service.decisionMaker.DecisionMakerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/decision-maker")
public class DecisionMakerController {

    @Autowired
    private DecisionMakerService decisionMakerService;

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<DashboardDTO>> getDashboard() {
        DashboardDTO dashboard = decisionMakerService.getDashboard();
        return ResponseEntity.ok(ApiResponse.success(dashboard));
    }

    @GetMapping("/reports")
    public ResponseEntity<ApiResponse<IPage<ReportDetailDTO>>> listReports(
            @RequestParam(required = false) String province,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        
        IPage<ReportDetailDTO> reports = decisionMakerService.listReports(province, city, status, pageNum, pageSize);
        return ResponseEntity.ok(ApiResponse.success(reports));
    }

    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<List<StatisticsDTO>>> getStatisticsByMonth(
            @RequestParam(required = false) String province,
            @RequestParam(required = false) String city,
            @RequestParam String month) {
        
        List<StatisticsDTO> statistics = decisionMakerService.getStatisticsByMonth(province, city, month);
        return ResponseEntity.ok(ApiResponse.success(statistics));
    }

    @GetMapping("/statistics/trend")
    public ResponseEntity<ApiResponse<List<StatisticsDTO>>> getStatisticsTrend(
            @RequestParam(required = false) String province,
            @RequestParam(required = false) String city,
            @RequestParam String startMonth,
            @RequestParam String endMonth) {
        
        List<StatisticsDTO> trend = decisionMakerService.getStatisticsTrend(province, city, startMonth, endMonth);
        return ResponseEntity.ok(ApiResponse.success(trend));
    }

    @GetMapping("/statistics/region")
    public ResponseEntity<ApiResponse<DashboardDTO.RegionStatistics>> getRegionStatistics(
            @RequestParam String province,
            @RequestParam(required = false) String city) {
        
        DashboardDTO.RegionStatistics statistics = decisionMakerService.getRegionStatistics(province, city);
        return ResponseEntity.ok(ApiResponse.success(statistics));
    }
}