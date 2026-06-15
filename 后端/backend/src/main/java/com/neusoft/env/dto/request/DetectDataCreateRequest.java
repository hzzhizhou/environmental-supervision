package com.neusoft.env.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DetectDataCreateRequest {

    @NotNull(message = "上报ID不能为空")
    private Integer reportId;

    private Integer userId;

    @NotNull(message = "AQI值不能为空")
    private Integer aqiValue;

    @NotBlank(message = "AQI等级不能为空")
    private String aqiLevel;

    @NotNull(message = "PM2.5不能为空")
    private BigDecimal pm25;

    @NotNull(message = "PM10不能为空")
    private BigDecimal pm10;

    @NotNull(message = "臭氧浓度不能为空")
    private BigDecimal o3;

    @NotNull(message = "二氧化硫浓度不能为空")
    private BigDecimal so2;

    @NotNull(message = "二氧化氮浓度不能为空")
    private BigDecimal no2;

    @NotNull(message = "一氧化碳浓度不能为空")
    private BigDecimal co;

    @NotBlank(message = "检测地点不能为空")
    private String location;

    private String remark;
}