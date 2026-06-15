package com.neusoft.env.service.supervisor.impl;

import com.neusoft.env.dto.response.PointsDTO;
import com.neusoft.env.dto.response.RankingDTO;
import com.neusoft.env.entity.User;
import com.neusoft.env.entity.UserPoints;
import com.neusoft.env.mapper.UserMapper;
import com.neusoft.env.mapper.UserPointsMapper;
import com.neusoft.env.service.supervisor.SupervisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SupervisorServiceImpl implements SupervisorService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserPointsMapper userPointsMapper;

    @Value("${file.upload-dir:uploads/files}")
    private String uploadDir;

    @Override
    public PointsDTO getPoints(Integer userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        UserPoints userPoints = userPointsMapper.selectById(userId);
        int points = userPoints != null ? userPoints.getTotalPoints() : 0;
        int rank = calculateRank(userId, points);

        return PointsDTO.builder()
                .userId(userId)
                .username(user.getUsername())
                .realName(user.getRealName())
                .totalPoints(points)
                .rank(rank)
                .build();
    }

    @Override
    public List<PointsDTO.PointLog> getPointsLogs(Integer userId) {
        List<PointsDTO.PointLog> logs = new ArrayList<>();
        logs.add(PointsDTO.PointLog.builder()
                .id(1)
                .userId(userId)
                .points(20)
                .type("REPORT")
                .description("上报空气质量数据")
                .createdAt(LocalDateTime.now().minusDays(1).toString())
                .build());
        logs.add(PointsDTO.PointLog.builder()
                .id(2)
                .userId(userId)
                .points(20)
                .type("REPORT")
                .description("上报空气质量数据")
                .createdAt(LocalDateTime.now().minusDays(2).toString())
                .build());
        return logs;
    }

    @Override
    public List<RankingDTO> getRanking() {
        List<User> users = userMapper.selectList(null);
        List<RankingDTO> ranking = new ArrayList<>();

        for (User user : users) {
            if (user.getRoleId() == 3) {
                UserPoints userPoints = userPointsMapper.selectById(user.getUserId());
                int points = userPoints != null ? userPoints.getTotalPoints() : 0;

                ranking.add(RankingDTO.builder()
                        .userId(user.getUserId())
                        .username(user.getUsername())
                        .realName(user.getRealName())
                        .points(points)
                        .avatarUrl(user.getAvatarUrl())
                        .build());
            }
        }

        ranking.sort((a, b) -> b.getPoints().compareTo(a.getPoints()));
        for (int i = 0; i < ranking.size(); i++) {
            ranking.get(i).setRank(i + 1);
        }

        return ranking;
    }

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : ".png";
            String newFilename = UUID.randomUUID().toString() + extension;

            Path filePath = uploadPath.resolve(newFilename);
            Files.copy(file.getInputStream(), filePath);

            return "/api/supervisor/upload/" + newFilename;
        } catch (IOException e) {
            throw new RuntimeException("文件上传失败", e);
        }
    }

    @Override
    @Transactional
    public void addPoints(Integer userId, Integer points, String type, String description, Integer relatedId, String relatedType) {
        UserPoints userPoints = userPointsMapper.selectById(userId);
        if (userPoints == null) {
            userPoints = UserPoints.builder()
                    .userId(userId)
                    .totalPoints(points)
                    .updatedAt(LocalDateTime.now())
                    .build();
            userPointsMapper.insert(userPoints);
        } else {
            userPointsMapper.addPoints(userId, points);
        }
    }

    private int calculateRank(Integer userId, int points) {
        List<User> users = userMapper.selectList(null);
        long count = users.stream()
                .filter(u -> u.getRoleId() == 3 && !u.getUserId().equals(userId))
                .map(u -> {
                    UserPoints up = userPointsMapper.selectById(u.getUserId());
                    return up != null ? up.getTotalPoints() : 0;
                })
                .filter(p -> p > points)
                .count();
        return (int) count + 1;
    }
}