package com.neusoft.env.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GridDTO {

    private Integer gridId;

    private String gridCode;

    private String gridName;

    private Integer provinceId;

    private String province;

    private Integer cityId;

    private String city;

    private String district;

    private String address;

    private Integer status;

    private String statusDesc;

    private LocalDateTime createdAt;
}