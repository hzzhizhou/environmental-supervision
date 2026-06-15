package com.neusoft.env.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("cities")
public class City {

    @TableId(value = "city_id", type = IdType.AUTO)
    private Integer cityId;

    private String cityCode;

    private String cityName;

    private Integer provinceId;

    private Integer status = 1;
}