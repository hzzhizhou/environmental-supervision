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
@TableName("grids")
public class Grid extends BaseEntity {

    @TableId(value = "grid_id", type = IdType.AUTO)
    private Integer gridId;

    private String gridCode;

    private String gridName;

    private Integer provinceId;

    private Integer cityId;

    private String district;

    private String address;

    private Integer status = 1;
}