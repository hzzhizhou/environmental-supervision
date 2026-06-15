package com.neusoft.env.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DetectDataDTO {

    private Integer detectId;

    private Integer taskId;

    private String reportCode;

    private Integer userId;

    private String userName;

    private LocalDateTime detectTime;

    private Integer aqiValue;

    private String aqiLevel;

    private BigDecimal pm25;

    private BigDecimal pm10;

    private BigDecimal o3;

    private BigDecimal so2;

    private BigDecimal no2;

    private BigDecimal co;

    private String location;

    private String photos;

    private String remark;

    private Integer status;

    private String statusDesc;

    private String confirmNote;

    private LocalDateTime confirmedAt;
}