package com.neusoft.env.service.supervisor;

import com.neusoft.env.dto.request.AqiReportCreateRequest;
import com.neusoft.env.dto.request.AqiReportQueryRequest;
import com.neusoft.env.dto.response.AqiReportDTO;
import com.neusoft.env.dto.response.PageResponse;

public interface SupervisorReportService {

    AqiReportDTO createReport(AqiReportCreateRequest request);

    AqiReportDTO getReportById(Integer reportId, Integer userId);

    PageResponse<AqiReportDTO> getReportList(AqiReportQueryRequest request);

    void deleteReport(Integer reportId, Integer userId);
}