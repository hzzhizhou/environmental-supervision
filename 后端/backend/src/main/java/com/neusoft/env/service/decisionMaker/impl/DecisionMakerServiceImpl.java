package com.neusoft.env.service.decisionMaker.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.env.dto.response.DashboardDTO;
import com.neusoft.env.dto.response.ReportDetailDTO;
import com.neusoft.env.dto.response.StatisticsDTO;
import com.neusoft.env.entity.AqiReport;
import com.neusoft.env.entity.DetectData;
import com.neusoft.env.entity.Grid;
import com.neusoft.env.entity.Statistics;
import com.neusoft.env.entity.TaskAssignment;
import com.neusoft.env.entity.User;
import com.neusoft.env.mapper.AqiReportMapper;
import com.neusoft.env.mapper.DetectDataMapper;
import com.neusoft.env.mapper.GridMapper;
import com.neusoft.env.mapper.StatisticsMapper;
import com.neusoft.env.mapper.TaskAssignmentMapper;
import com.neusoft.env.mapper.UserMapper;
import com.neusoft.env.service.decisionMaker.DecisionMakerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DecisionMakerServiceImpl implements DecisionMakerService {

    @Autowired
    private AqiReportMapper reportMapper;

    @Autowired
    private StatisticsMapper statisticsMapper;

    @Autowired
    private GridMapper gridMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DetectDataMapper detectDataMapper;

    @Autowired
    private TaskAssignmentMapper taskAssignmentMapper;

    @Override
    public DashboardDTO getDashboard() {
        DashboardDTO dashboard = new DashboardDTO();

        long totalReports = reportMapper.selectCount(null);
        long pendingReports = reportMapper.countByStatus("pending");
        long confirmedReports = reportMapper.countByStatus("confirmed");
        long processedReports = totalReports - pendingReports;

        dashboard.setTotalReports((int) totalReports);
        dashboard.setPendingReports((int) pendingReports);
        dashboard.setConfirmedReports((int) confirmedReports);
        dashboard.setProcessedReports((int) processedReports);

        LambdaQueryWrapper<DetectData> detectQuery = new LambdaQueryWrapper<>();
        detectQuery.eq(DetectData::getStatus, 1);
        List<DetectData> confirmedDetects = detectDataMapper.selectList(detectQuery);
        
        if (!confirmedDetects.isEmpty()) {
            BigDecimal avgAqi = confirmedDetects.stream()
                    .map(DetectData::getAqiValue)
                    .map(BigDecimal::valueOf)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(confirmedDetects.size()), 2, RoundingMode.HALF_UP);
            dashboard.setAvgAqi(avgAqi);

            Integer maxAqi = confirmedDetects.stream()
                    .map(DetectData::getAqiValue)
                    .max(Integer::compareTo)
                    .orElse(0);
            dashboard.setMaxAqi(maxAqi);

            Integer minAqi = confirmedDetects.stream()
                    .map(DetectData::getAqiValue)
                    .min(Integer::compareTo)
                    .orElse(0);
            dashboard.setMinAqi(minAqi);
        }

        LocalDate today = LocalDate.now();
        String currentMonth = today.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        Statistics currentStat = statisticsMapper.findFirstByStatMonth(currentMonth);
        if (currentStat != null) {
            dashboard.setExcellentDays(currentStat.getExcellentDays());
            dashboard.setGoodDays(currentStat.getGoodDays());
            dashboard.setPollutionDays(
                    currentStat.getLightPollutionDays() +
                    currentStat.getModeratePollutionDays() +
                    currentStat.getHeavyPollutionDays()
            );
        }

        dashboard.setTopRegions(getTopRegions());
        dashboard.setMonthlyTrend(getMonthlyTrend());

        return dashboard;
    }

    @Override
    public IPage<ReportDetailDTO> listReports(String province, String city, String status, Integer pageNum, Integer pageSize) {
        Page<AqiReport> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<AqiReport> query = new LambdaQueryWrapper<>();
        
        if (province != null && !province.isEmpty()) {
            query.eq(AqiReport::getProvince, province);
            if (city != null && !city.isEmpty()) {
                query.eq(AqiReport::getCity, city);
            }
        } else if (status != null && !status.isEmpty()) {
            query.eq(AqiReport::getStatus, status);
        }
        query.orderByDesc(AqiReport::getReportedAt);
        
        IPage<AqiReport> reportPage = reportMapper.selectPage(page, query);
        Page<ReportDetailDTO> resultPage = new Page<>(reportPage.getCurrent(), reportPage.getSize(), reportPage.getTotal());
        resultPage.setRecords(reportPage.getRecords().stream().map(this::convertToReportDTO).collect(Collectors.toList()));
        return resultPage;
    }

    @Override
    public List<StatisticsDTO> getStatisticsByMonth(String province, String city, String month) {
        List<Statistics> stats;

        if (province != null && !province.isEmpty()) {
            if (city != null && !city.isEmpty()) {
                stats = statisticsMapper.findByProvinceAndCityAndStatMonth(province, city, month);
            } else {
                stats = statisticsMapper.findByProvinceAndStatMonth(province, month);
            }
        } else {
            stats = statisticsMapper.findByStatMonth(month);
        }

        return stats.stream().map(this::convertToStatDTO).collect(Collectors.toList());
    }

    @Override
    public List<StatisticsDTO> getStatisticsTrend(String province, String city, String startMonth, String endMonth) {
        YearMonth start = YearMonth.parse(startMonth);
        YearMonth end = YearMonth.parse(endMonth);

        List<StatisticsDTO> trend = new ArrayList<>();
        YearMonth current = start;

        while (!current.isAfter(end)) {
            String monthStr = current.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            List<Statistics> stats;

            if (province != null && !province.isEmpty()) {
                if (city != null && !city.isEmpty()) {
                    stats = statisticsMapper.findByProvinceAndCityAndStatMonth(province, city, monthStr);
                } else {
                    stats = statisticsMapper.findByProvinceAndStatMonth(province, monthStr);
                }
            } else {
                stats = statisticsMapper.findByStatMonth(monthStr);
            }

            if (!stats.isEmpty()) {
                trend.add(convertToStatDTO(stats.get(0)));
            }

            current = current.plusMonths(1);
        }

        return trend;
    }

    @Override
    public DashboardDTO.RegionStatistics getRegionStatistics(String province, String city) {
        DashboardDTO.RegionStatistics regionStat = new DashboardDTO.RegionStatistics();
        regionStat.setProvince(province);
        regionStat.setCity(city);

        LambdaQueryWrapper<DetectData> detectQuery = new LambdaQueryWrapper<>();
        detectQuery.eq(DetectData::getStatus, 1);
        List<DetectData> confirmedDetects = detectDataMapper.selectList(detectQuery);

        List<Integer> taskIds = confirmedDetects.stream()
                .map(DetectData::getTaskId)
                .distinct()
                .collect(Collectors.toList());

        if (taskIds.isEmpty()) {
            regionStat.setReportCount(0);
            return regionStat;
        }

        LambdaQueryWrapper<TaskAssignment> taskQuery = new LambdaQueryWrapper<>();
        taskQuery.in(TaskAssignment::getTaskId, taskIds);
        List<TaskAssignment> tasks = taskAssignmentMapper.selectList(taskQuery);

        List<Integer> reportIds = tasks.stream()
                .map(TaskAssignment::getReportId)
                .distinct()
                .collect(Collectors.toList());

        LambdaQueryWrapper<AqiReport> reportQuery = new LambdaQueryWrapper<>();
        reportQuery.in(AqiReport::getReportId, reportIds);
        reportQuery.eq(AqiReport::getProvince, province);
        if (city != null && !city.isEmpty()) {
            reportQuery.eq(AqiReport::getCity, city);
        }
        List<AqiReport> reportList = reportMapper.selectList(reportQuery);

        List<Integer> filteredReportIds = reportList.stream()
                .map(AqiReport::getReportId)
                .collect(Collectors.toList());

        List<Integer> filteredTaskIds = tasks.stream()
                .filter(t -> filteredReportIds.contains(t.getReportId()))
                .map(TaskAssignment::getTaskId)
                .collect(Collectors.toList());

        List<DetectData> regionDetects = confirmedDetects.stream()
                .filter(d -> filteredTaskIds.contains(d.getTaskId()))
                .collect(Collectors.toList());

        regionStat.setReportCount(regionDetects.size());

        if (!regionDetects.isEmpty()) {
            BigDecimal avgAqi = regionDetects.stream()
                    .map(DetectData::getAqiValue)
                    .map(BigDecimal::valueOf)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(regionDetects.size()), 2, RoundingMode.HALF_UP);
            regionStat.setAvgAqi(avgAqi);
        }

        return regionStat;
    }

    private List<DashboardDTO.RegionStatistics> getTopRegions() {
        LambdaQueryWrapper<DetectData> detectQuery = new LambdaQueryWrapper<>();
        detectQuery.eq(DetectData::getStatus, 1);
        List<DetectData> confirmedDetects = detectDataMapper.selectList(detectQuery);

        if (confirmedDetects.isEmpty()) {
            return new ArrayList<>();
        }

        List<Integer> taskIds = confirmedDetects.stream()
                .map(DetectData::getTaskId)
                .distinct()
                .collect(Collectors.toList());

        LambdaQueryWrapper<TaskAssignment> taskQuery = new LambdaQueryWrapper<>();
        taskQuery.in(TaskAssignment::getTaskId, taskIds);
        List<TaskAssignment> tasks = taskAssignmentMapper.selectList(taskQuery);

        List<Integer> reportIds = tasks.stream()
                .map(TaskAssignment::getReportId)
                .distinct()
                .collect(Collectors.toList());

        LambdaQueryWrapper<AqiReport> reportQuery = new LambdaQueryWrapper<>();
        reportQuery.in(AqiReport::getReportId, reportIds);
        List<AqiReport> reports = reportMapper.selectList(reportQuery);

        Map<Integer, AqiReport> reportMap = reports.stream()
                .collect(Collectors.toMap(AqiReport::getReportId, r -> r));

        Map<Integer, TaskAssignment> taskMap = tasks.stream()
                .collect(Collectors.toMap(TaskAssignment::getTaskId, t -> t));

        Map<String, List<DetectData>> groupedByRegion = confirmedDetects.stream()
                .filter(d -> {
                    TaskAssignment task = taskMap.get(d.getTaskId());
                    return task != null && reportMap.containsKey(task.getReportId());
                })
                .collect(Collectors.groupingBy(d -> {
                    TaskAssignment task = taskMap.get(d.getTaskId());
                    AqiReport report = reportMap.get(task.getReportId());
                    return report.getProvince() + "_" + report.getCity();
                }));

        return groupedByRegion.entrySet().stream()
                .map(entry -> {
                    String[] parts = entry.getKey().split("_");
                    String province = parts[0];
                    String city = parts.length > 1 ? parts[1] : "";
                    List<DetectData> regionDetects = entry.getValue();

                    BigDecimal avgAqi = regionDetects.stream()
                            .map(DetectData::getAqiValue)
                            .map(BigDecimal::valueOf)
                            .reduce(BigDecimal.ZERO, BigDecimal::add)
                            .divide(BigDecimal.valueOf(regionDetects.size()), 2, RoundingMode.HALF_UP);

                    DashboardDTO.RegionStatistics stat = new DashboardDTO.RegionStatistics();
                    stat.setProvince(province);
                    stat.setCity(city);
                    stat.setReportCount(regionDetects.size());
                    stat.setAvgAqi(avgAqi);
                    return stat;
                })
                .sorted((a, b) -> Integer.compare(b.getReportCount(), a.getReportCount()))
                .limit(5)
                .collect(Collectors.toList());
    }

    private List<DashboardDTO.MonthlyTrend> getMonthlyTrend() {
        YearMonth current = YearMonth.now();
        List<DashboardDTO.MonthlyTrend> trend = new ArrayList<>();

        for (int i = 5; i >= 0; i--) {
            YearMonth month = current.minusMonths(i);
            String monthStr = month.format(DateTimeFormatter.ofPattern("yyyy-MM"));

            Statistics stat = statisticsMapper.findFirstByStatMonth(monthStr);

            DashboardDTO.MonthlyTrend trendItem = new DashboardDTO.MonthlyTrend();
            trendItem.setMonth(monthStr);

            if (stat != null) {
                trendItem.setReportCount(stat.getTotalReports());
                trendItem.setAvgAqi(stat.getAvgAqi());
            } else {
                LambdaQueryWrapper<DetectData> detectQuery = new LambdaQueryWrapper<>();
                detectQuery.eq(DetectData::getStatus, 1);
                List<DetectData> confirmedDetects = detectDataMapper.selectList(detectQuery);

                List<DetectData> monthDetects = confirmedDetects.stream()
                        .filter(d -> {
                            if (d.getDetectTime() != null) {
                                YearMonth detectMonth = YearMonth.from(d.getDetectTime());
                                return detectMonth.equals(month);
                            }
                            return false;
                        })
                        .collect(Collectors.toList());

                trendItem.setReportCount(monthDetects.size());

                if (!monthDetects.isEmpty()) {
                    BigDecimal avgAqi = monthDetects.stream()
                            .map(DetectData::getAqiValue)
                            .map(BigDecimal::valueOf)
                            .reduce(BigDecimal.ZERO, BigDecimal::add)
                            .divide(BigDecimal.valueOf(monthDetects.size()), 2, RoundingMode.HALF_UP);
                    trendItem.setAvgAqi(avgAqi);
                }
            }

            trend.add(trendItem);
        }

        return trend;
    }

    private ReportDetailDTO convertToReportDTO(AqiReport report) {
        ReportDetailDTO dto = new ReportDetailDTO();
        dto.setReportId(report.getReportId());
        dto.setReportCode(report.getReportCode());
        dto.setUserId(report.getUserId());
        dto.setAqiValue(report.getAqiValue());
        dto.setAqiLevel(report.getAqiLevel());
        dto.setProvince(report.getProvince());
        dto.setCity(report.getCity());
        dto.setAddress(report.getAddress());
        dto.setGridId(report.getGridId());
        dto.setStatus(report.getStatus());
        dto.setStatusDesc(getStatusDesc(report.getStatus()));
        dto.setRemark(report.getRemark());
        dto.setPm25(report.getPm25());
        dto.setPm10(report.getPm10());
        dto.setO3(report.getO3());
        dto.setSo2(report.getSo2());
        dto.setNo2(report.getNo2());
        dto.setCo(report.getCo());
        dto.setReportedAt(report.getReportedAt());
        dto.setConfirmedAt(report.getConfirmedAt());

        User user = userMapper.selectById(report.getUserId());
        if (user != null) {
            dto.setUserName(user.getRealName());
        }
        Grid grid = gridMapper.selectById(report.getGridId());
        if (grid != null) {
            dto.setGridName(grid.getGridName());
        }

        return dto;
    }

    private StatisticsDTO convertToStatDTO(Statistics stat) {
        StatisticsDTO dto = new StatisticsDTO();
        dto.setStatId(stat.getStatId());
        dto.setProvince(stat.getProvince());
        dto.setCity(stat.getCity());
        dto.setStatMonth(stat.getStatMonth());
        dto.setTotalReports(stat.getTotalReports());
        dto.setConfirmedCount(stat.getConfirmedCount());
        dto.setAvgAqi(stat.getAvgAqi());
        dto.setMaxAqi(stat.getMaxAqi());
        dto.setMinAqi(stat.getMinAqi());
        dto.setExcellentDays(stat.getExcellentDays());
        dto.setGoodDays(stat.getGoodDays());
        dto.setLightPollutionDays(stat.getLightPollutionDays());
        dto.setModeratePollutionDays(stat.getModeratePollutionDays());
        dto.setHeavyPollutionDays(stat.getHeavyPollutionDays());
        dto.setUpdatedAt(stat.getUpdatedAt());
        return dto;
    }

    private String getStatusDesc(String status) {
        return switch (status) {
            case "pending" -> "待指派";
            case "assigned" -> "已指派";
            case "detected" -> "已检测";
            case "confirmed" -> "已确认";
            case "unconfirmed" -> "不合理";
            default -> status;
        };
    }
}