package com.neusoft.env.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AqiReportDTO {

    private Integer reportId;

    private String reportCode;

    private Integer userId;

    private String realName;

    private String phone;

    private String pollutionType;

    private String pollutionLevel;

    private String pollutionDesc;

    private List<String> photos;

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