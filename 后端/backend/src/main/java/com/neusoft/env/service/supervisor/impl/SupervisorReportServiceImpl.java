package com.neusoft.env.service.supervisor.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.env.dto.request.AqiReportCreateRequest;
import com.neusoft.env.dto.request.AqiReportQueryRequest;
import com.neusoft.env.dto.response.AqiReportDTO;
import com.neusoft.env.dto.response.PageResponse;
import com.neusoft.env.entity.AqiReport;
import com.neusoft.env.entity.City;
import com.neusoft.env.entity.Grid;
import com.neusoft.env.entity.Province;
import com.neusoft.env.entity.User;
import com.neusoft.env.mapper.AqiReportMapper;
import com.neusoft.env.mapper.CityMapper;
import com.neusoft.env.mapper.GridMapper;
import com.neusoft.env.mapper.ProvinceMapper;
import com.neusoft.env.mapper.UserMapper;
import com.neusoft.env.service.supervisor.SupervisorReportService;
import com.neusoft.env.service.supervisor.SupervisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SupervisorReportServiceImpl implements SupervisorReportService {

    private static final int REPORT_POINTS = 20;

    private final AqiReportMapper aqiReportMapper;
    private final UserMapper userMapper;
    private final GridMapper gridMapper;
    private final ProvinceMapper provinceMapper;
    private final CityMapper cityMapper;
    private final SupervisorService supervisorService;

    private static final Map<String, String> STATUS_DESC_MAP = Map.of(
            "pending", "待指派",
            "assigned", "已指派",
            "detected", "已检测",
            "confirmed", "已确认"
    );

    @Autowired
    public SupervisorReportServiceImpl(AqiReportMapper aqiReportMapper,
                                       UserMapper userMapper,
                                       GridMapper gridMapper,
                                       ProvinceMapper provinceMapper,
                                       CityMapper cityMapper,
                                       @Lazy SupervisorService supervisorService) {
        this.aqiReportMapper = aqiReportMapper;
        this.userMapper = userMapper;
        this.gridMapper = gridMapper;
        this.provinceMapper = provinceMapper;
        this.cityMapper = cityMapper;
        this.supervisorService = supervisorService;
    }

    @Override
    @Transactional
    public AqiReportDTO createReport(AqiReportCreateRequest request) {
        User user = userMapper.selectById(request.getUserId());

        AqiReport report = new AqiReport();
        report.setReportCode(generateReportCode());
        report.setUserId(request.getUserId());
        report.setPollutionType(request.getPollutionType());
        report.setPollutionLevel(request.getPollutionLevel());
        report.setPollutionDesc(request.getPollutionDesc());
        report.setProvince(request.getProvince());
        report.setCity(request.getCity());
        report.setAddress(request.getAddress());
        report.setRemark(request.getRemark());
        report.setPhotos(request.getPhotos());
        report.setAqiValue(request.getAqiValue());
        report.setAqiLevel(request.getAqiLevel());
        report.setPm25(request.getPm25());
        report.setPm10(request.getPm10());
        report.setO3(request.getO3());
        report.setSo2(request.getSo2());
        report.setNo2(request.getNo2());
        report.setCo(request.getCo());
        report.setStatus("pending");
        report.setReportedAt(LocalDateTime.now());

        Integer gridId = findGridId(request.getProvince(), request.getCity(), request.getAddress());
        report.setGridId(gridId);

        aqiReportMapper.insert(report);

        supervisorService.addPoints(request.getUserId(), REPORT_POINTS, "REPORT", "上报环境监督举报获得积分", report.getReportId(), "POLLUTION_REPORT");
        
        Grid grid = gridId != null ? gridMapper.selectById(gridId) : null;
        return convertToDTO(report, user, grid);
    }

    @Override
    public AqiReportDTO getReportById(Integer reportId, Integer userId) {
        AqiReport report = aqiReportMapper.selectById(reportId);
        if (report == null) {
            throw new RuntimeException("上报记录不存在");
        }

        if (!report.getUserId().equals(userId)) {
            throw new RuntimeException("无权查看此上报记录");
        }

        User user = userMapper.selectById(userId);
        Grid grid = report.getGridId() != null ? gridMapper.selectById(report.getGridId()) : null;

        return convertToDTO(report, user, grid);
    }

    @Override
    public PageResponse<AqiReportDTO> getReportList(AqiReportQueryRequest request) {
        Page<AqiReport> page = new Page<>(request.getPageNum(), request.getPageSize());
        LambdaQueryWrapper<AqiReport> query = buildQueryWrapper(request);
        query.orderByDesc(AqiReport::getReportedAt);
        
        IPage<AqiReport> reportPage = aqiReportMapper.selectPage(page, query);

        List<Integer> userIds = reportPage.getRecords().stream()
                .map(AqiReport::getUserId)
                .distinct()
                .collect(Collectors.toList());
        Map<Integer, User> userMap = userIds.stream()
                .map(id -> userMapper.selectById(id))
                .filter(u -> u != null)
                .collect(Collectors.toMap(User::getUserId, u -> u));

        List<Integer> gridIds = reportPage.getRecords().stream()
                .map(AqiReport::getGridId)
                .filter(id -> id != null)
                .distinct()
                .collect(Collectors.toList());
        Map<Integer, Grid> gridMap = gridIds.stream()
                .map(id -> gridMapper.selectById(id))
                .filter(g -> g != null)
                .collect(Collectors.toMap(Grid::getGridId, g -> g));

        List<AqiReportDTO> dtoList = reportPage.getRecords().stream()
                .map(report -> {
                    User user = userMap.get(report.getUserId());
                    Grid grid = report.getGridId() != null ? gridMap.get(report.getGridId()) : null;
                    return convertToDTO(report, user, grid);
                })
                .collect(Collectors.toList());

        return PageResponse.<AqiReportDTO>builder()
                .content(dtoList)
                .totalElements(reportPage.getTotal())
                .totalPages((int) reportPage.getPages())
                .currentPage(request.getPageNum())
                .pageSize(request.getPageSize())
                .build();
    }

    @Override
    @Transactional
    public void deleteReport(Integer reportId, Integer userId) {
        AqiReport report = aqiReportMapper.selectById(reportId);
        if (report == null) {
            throw new RuntimeException("上报记录不存在");
        }

        if (!report.getUserId().equals(userId)) {
            throw new RuntimeException("无权删除此上报记录");
        }

        aqiReportMapper.deleteById(reportId);
    }

    private String generateReportCode() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long count = aqiReportMapper.countAll();
        return String.format("AQI-%s-%03d", dateStr, count + 1);
    }

    private Integer findGridId(String province, String city, String address) {
        LambdaQueryWrapper<Province> provinceQuery = new LambdaQueryWrapper<>();
        provinceQuery.eq(Province::getProvinceName, province);
        Province provinceEntity = provinceMapper.selectOne(provinceQuery);
        if (provinceEntity == null) {
            return null;
        }

        LambdaQueryWrapper<City> cityQuery = new LambdaQueryWrapper<>();
        cityQuery.eq(City::getCityName, city);
        cityQuery.eq(City::getProvinceId, provinceEntity.getProvinceId());
        City cityEntity = cityMapper.selectOne(cityQuery);
        if (cityEntity == null) {
            return null;
        }

        List<Grid> grids = gridMapper.findByProvinceAndCity(provinceEntity.getProvinceId(), cityEntity.getCityId());
        if (grids.isEmpty()) {
            return null;
        }
        if (grids.size() == 1) {
            return grids.get(0).getGridId();
        }
        if (StringUtils.hasText(address)) {
            for (Grid grid : grids) {
                if (grid.getDistrict() != null && address.contains(grid.getDistrict())) {
                    return grid.getGridId();
                }
            }
        }
        return grids.get(0).getGridId();
    }

    private LambdaQueryWrapper<AqiReport> buildQueryWrapper(AqiReportQueryRequest request) {
        LambdaQueryWrapper<AqiReport> query = new LambdaQueryWrapper<>();

        if (request.getUserId() != null) {
            query.eq(AqiReport::getUserId, request.getUserId());
        }

        if (StringUtils.hasText(request.getStatus())) {
            query.eq(AqiReport::getStatus, request.getStatus());
        }

        if (StringUtils.hasText(request.getProvince())) {
            query.eq(AqiReport::getProvince, request.getProvince());
        }

        if (StringUtils.hasText(request.getCity())) {
            query.eq(AqiReport::getCity, request.getCity());
        }

        if (StringUtils.hasText(request.getKeyword())) {
            String keyword = request.getKeyword();
            query.like(AqiReport::getAddress, keyword).or().like(AqiReport::getRemark, keyword);
        }

        return query;
    }

    private AqiReportDTO convertToDTO(AqiReport report, User user, Grid grid) {
        List<String> photos = new ArrayList<>();
        if (StringUtils.hasText(report.getPhotos())) {
            try {
                photos = java.util.Arrays.asList(report.getPhotos().split(","));
            } catch (Exception e) {
                photos.add(report.getPhotos());
            }
        }

        return AqiReportDTO.builder()
                .reportId(report.getReportId())
                .reportCode(report.getReportCode())
                .userId(report.getUserId())
                .realName(user != null ? user.getRealName() : null)
                .phone(user != null ? user.getPhone() : null)
                .pollutionType(report.getPollutionType())
                .pollutionLevel(report.getPollutionLevel())
                .pollutionDesc(report.getPollutionDesc())
                .photos(photos)
                .aqiValue(report.getAqiValue())
                .aqiLevel(report.getAqiLevel())
                .province(report.getProvince())
                .city(report.getCity())
                .address(report.getAddress())
                .gridId(report.getGridId())
                .gridName(grid != null ? grid.getGridName() : null)
                .status(report.getStatus())
                .statusDesc(STATUS_DESC_MAP.getOrDefault(report.getStatus(), report.getStatus()))
                .remark(report.getRemark())
                .pm25(report.getPm25())
                .pm10(report.getPm10())
                .o3(report.getO3())
                .so2(report.getSo2())
                .no2(report.getNo2())
                .co(report.getCo())
                .reportedAt(report.getReportedAt())
                .confirmedAt(report.getConfirmedAt())
                .build();
    }
}