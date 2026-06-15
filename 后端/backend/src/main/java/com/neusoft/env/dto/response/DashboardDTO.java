package com.neusoft.env.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class DashboardDTO {

    private Integer totalReports;

    private Integer pendingReports;

    private Integer confirmedReports;

    private Integer processedReports;

    private BigDecimal avgAqi;

    private Integer maxAqi;

    private Integer minAqi;

    private Integer excellentDays;

    private Integer goodDays;

    private Integer pollutionDays;

    private List<RegionStatistics> topRegions;

    private List<MonthlyTrend> monthlyTrend;

    @Data
    public static class RegionStatistics {
        private String province;
        private String city;
        private Integer reportCount;
        private BigDecimal avgAqi;
    }

    @Data
    public static class MonthlyTrend {
        private String month;
        private Integer reportCount;
        private BigDecimal avgAqi;
    }
}