package com.neusoft.env.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AqiReportQueryRequest {

    private Integer userId;

    private String status;

    private String province;

    private String city;

    private String keyword;

    private Integer pageNum = 1;

    private Integer pageSize = 10;
}