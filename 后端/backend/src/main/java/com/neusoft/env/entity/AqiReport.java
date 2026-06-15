package com.neusoft.env.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("aqi_reports")
public class AqiReport {

    @TableId(value = "report_id", type = IdType.AUTO)
    private Integer reportId;

    private String reportCode;

    private Integer userId;

    private Integer aqiValue;

    private String aqiLevel;

    private String province;

    private String city;

    private String address;

    private Integer gridId;

    private String status = "pending";

    private String remark;

    private String pollutionType;

    private String pollutionLevel;

    private String pollutionDesc;

    private String photos;

    private BigDecimal pm25;

    private BigDecimal pm10;

    private BigDecimal o3;

    private BigDecimal so2;

    private BigDecimal no2;

    private BigDecimal co;

    private LocalDateTime reportedAt;

    private LocalDateTime confirmedAt;
}