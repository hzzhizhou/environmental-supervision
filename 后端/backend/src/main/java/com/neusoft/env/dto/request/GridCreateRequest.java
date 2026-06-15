package com.neusoft.env.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GridCreateRequest {

    @NotBlank(message = "网格编码不能为空")
    private String gridCode;

    @NotBlank(message = "网格名称不能为空")
    private String gridName;

    @NotNull(message = "省份ID不能为空")
    private Integer provinceId;

    @NotNull(message = "城市ID不能为空")
    private Integer cityId;

    private String district;

    private String address;

    private Integer status = 1;
}