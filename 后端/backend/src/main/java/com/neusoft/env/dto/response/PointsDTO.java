package com.neusoft.env.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointsDTO {

    private Integer userId;
    private String username;
    private String realName;
    private Integer totalPoints;
    private Integer rank;
    private List<PointLog> logs;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PointLog {
        private Integer id;
        private Integer userId;
        private Integer points;
        private String type;
        private String description;
        private String createdAt;
    }
}