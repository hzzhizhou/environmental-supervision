package com.neusoft.env.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ReportDetailDTO {

    private Integer reportId;

    private String reportCode;

    private Integer userId;

    private String userName;

    private String pollutionType;

    private String pollutionLevel;

    private String pollutionDesc;

    private String photos;

    private Integer aqiValue;

    private String aqiLevel;

    private String province;

    private String city;

    private String address;

    private Integer gridId;

    private String gridName;

    private String status;

    private String statusDesc;

    private String remark;

    private BigDecimal pm25;

    private BigDecimal pm10;

    private BigDecimal o3;

    private BigDecimal so2;

    private BigDecimal no2;

    private BigDecimal co;

    private LocalDateTime reportedAt;

    private LocalDateTime confirmedAt;
}