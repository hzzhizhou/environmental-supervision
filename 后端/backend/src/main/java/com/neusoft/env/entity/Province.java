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
@TableName("provinces")
public class Province {

    @TableId(value = "province_id", type = IdType.AUTO)
    private Integer provinceId;

    private String provinceCode;

    private String provinceName;

    private Integer status = 1;
}