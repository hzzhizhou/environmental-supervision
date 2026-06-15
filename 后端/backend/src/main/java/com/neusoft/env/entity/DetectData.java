package com.neusoft.env.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("detect_data")
public class DetectData {

    @TableId(value = "detect_id", type = IdType.AUTO)
    private Integer detectId;

    private Integer taskId;

    private Integer userId;

    @TableField("detected_at")
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

    @TableField("photo_url")
    private String photos;

    private String remark;

    @TableField("status")
    private Integer status = 0;

    @TableField("confirm_note")
    private String confirmNote;

    @TableField("audited_at")
    private LocalDateTime confirmedAt;
}