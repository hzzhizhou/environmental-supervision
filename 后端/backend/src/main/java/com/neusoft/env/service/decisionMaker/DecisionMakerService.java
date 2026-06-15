package com.neusoft.env.service.decisionMaker;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.neusoft.env.dto.response.DashboardDTO;
import com.neusoft.env.dto.response.ReportDetailDTO;
import com.neusoft.env.dto.response.StatisticsDTO;

import java.util.List;

public interface DecisionMakerService {

    DashboardDTO getDashboard();

    IPage<ReportDetailDTO> listReports(String province, String city, String status, Integer pageNum, Integer pageSize);

    List<StatisticsDTO> getStatisticsByMonth(String province, String city, String month);

    List<StatisticsDTO> getStatisticsTrend(String province, String city, String startMonth, String endMonth);

    DashboardDTO.RegionStatistics getRegionStatistics(String province, String city);
}