package com.neusoft.env.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AqiReportCreateRequest {

    private Integer userId;

    @NotBlank(message = "污染类型不能为空")
    @Size(max = 50, message = "污染类型不能超过50个字符")
    private String pollutionType;

    @NotBlank(message = "污染程度不能为空")
    @Size(max = 20, message = "污染程度不能超过20个字符")
    private String pollutionLevel;

    @NotBlank(message = "污染描述不能为空")
    @Size(max = 500, message = "污染描述不能超过500个字符")
    private String pollutionDesc;

    @NotBlank(message = "省份不能为空")
    @Size(max = 30, message = "省份名称不能超过30个字符")
    private String province;

    @NotBlank(message = "城市不能为空")
    @Size(max = 30, message = "城市名称不能超过30个字符")
    private String city;

    @Size(max = 200, message = "详细地址不能超过200个字符")
    private String address;

    private String photos;

    @Size(max = 500, message = "备注不能超过500个字符")
    private String remark;

    private Integer aqiValue;

    private String aqiLevel;

    private BigDecimal pm25;

    private BigDecimal pm10;

    private BigDecimal o3;

    private BigDecimal so2;

    private BigDecimal no2;

    private BigDecimal co;
}