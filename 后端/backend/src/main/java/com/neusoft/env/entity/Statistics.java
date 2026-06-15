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
@TableName("statistics")
public class Statistics extends BaseEntity {

    @TableId(value = "stat_id", type = IdType.AUTO)
    private Integer statId;

    private String province;

    private String city;

    private String statMonth;

    private Integer totalReports = 0;

    private Integer confirmedCount = 0;

    private BigDecimal avgAqi;

    private Integer maxAqi;

    private Integer minAqi;

    private Integer excellentDays = 0;

    private Integer goodDays = 0;

    private Integer lightPollutionDays = 0;

    private Integer moderatePollutionDays = 0;

    private Integer heavyPollutionDays = 0;

    private LocalDateTime updatedAt;
}