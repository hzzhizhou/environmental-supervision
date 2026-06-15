package com.neusoft.env.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class StatisticsDTO {

    private Integer statId;

    private String province;

    private String city;

    private String statMonth;

    private Integer totalReports;

    private Integer confirmedCount;

    private BigDecimal avgAqi;

    private Integer maxAqi;

    private Integer minAqi;

    private Integer excellentDays;

    private Integer goodDays;

    private Integer lightPollutionDays;

    private Integer moderatePollutionDays;

    private Integer heavyPollutionDays;

    private LocalDateTime updatedAt;
}