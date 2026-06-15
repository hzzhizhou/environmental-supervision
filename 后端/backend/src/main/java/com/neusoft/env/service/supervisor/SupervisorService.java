package com.neusoft.env.service.supervisor;

import com.neusoft.env.dto.response.PointsDTO;
import com.neusoft.env.dto.response.RankingDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SupervisorService {

    PointsDTO getPoints(Integer userId);

    List<PointsDTO.PointLog> getPointsLogs(Integer userId);

    List<RankingDTO> getRanking();

    String uploadFile(MultipartFile file);

    void addPoints(Integer userId, Integer points, String type, String description, Integer relatedId, String relatedType);
}