package com.neusoft.env.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RankingDTO {

    private Integer rank;
    private Integer userId;
    private String username;
    private String realName;
    private Integer points;
    private String avatarUrl;
}