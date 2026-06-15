package com.neusoft.env.service.admin.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.env.dto.request.*;
import com.neusoft.env.dto.response.*;
import com.neusoft.env.entity.*;
import com.neusoft.env.mapper.*;
import com.neusoft.env.service.admin.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private GridMapper gridMapper;

    @Autowired
    private ProvinceMapper provinceMapper;

    @Autowired
    private CityMapper cityMapper;

    @Autowired
    private AqiReportMapper reportMapper;

    @Autowired
    private TaskAssignmentMapper taskAssignmentMapper;

    @Autowired
    private DetectDataMapper detectDataMapper;

    @Autowired
    private AuditLogMapper auditLogMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDTO createUser(UserCreateRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRealName(request.getRealName());
        user.setPhone(request.getPhone());
        user.setRoleId(request.getRoleId());
        user.setGridId(request.getGridId());
        user.setStatus(request.getStatus() != null ? request.getStatus() : 1);
        user.setCreatedAt(LocalDateTime.now());
        userMapper.insert(user);
        return convertToUserDTO(user);
    }

    @Override
    public UserDTO getUserById(Integer userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        return convertToUserDTO(user);
    }

    @Override
    public IPage<UserDTO> listUsers(String username, Integer roleId, Integer status, Integer pageNum, Integer pageSize) {
        Page<User> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<User> query = new LambdaQueryWrapper<>();
        
        if (username != null && !username.isEmpty()) {
            query.like(User::getUsername, username);
        }
        if (roleId != null) {
            query.eq(User::getRoleId, roleId);
        }
        if (status != null) {
            query.eq(User::getStatus, status);
        }
        query.orderByDesc(User::getCreatedAt);
        
        IPage<User> userPage = userMapper.selectPage(page, query);
        Page<UserDTO> resultPage = new Page<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        resultPage.setRecords(userPage.getRecords().stream().map(this::convertToUserDTO).collect(Collectors.toList()));
        return resultPage;
    }

    @Override
    @Transactional
    public UserDTO updateUser(Integer userId, UserUpdateRequest request) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        if (request.getRealName() != null) {
            user.setRealName(request.getRealName());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getRoleId() != null) {
            user.setRoleId(request.getRoleId());
        }
        if (request.getGridId() != null) {
            user.setGridId(request.getGridId());
        }
        if (request.getStatus() != null) {
            user.setStatus(request.getStatus());
        }
        if (request.getAvatarUrl() != null) {
            user.setAvatarUrl(request.getAvatarUrl());
        }
        
        userMapper.updateById(user);
        return convertToUserDTO(user);
    }

    @Override
    @Transactional
    public void deleteUser(Integer userId) {
        if (userMapper.selectById(userId) == null) {
            throw new RuntimeException("用户不存在");
        }
        userMapper.deleteById(userId);
    }

    @Override
    @Transactional
    public GridDTO createGrid(GridCreateRequest request) {
        Grid grid = new Grid();
        grid.setGridCode(request.getGridCode());
        grid.setGridName(request.getGridName());
        grid.setProvinceId(request.getProvinceId());
        grid.setCityId(request.getCityId());
        grid.setDistrict(request.getDistrict());
        grid.setAddress(request.getAddress());
        grid.setStatus(request.getStatus() != null ? request.getStatus() : 1);
        grid.setCreatedAt(LocalDateTime.now());
        gridMapper.insert(grid);
        return convertToGridDTO(grid);
    }

    @Override
    public GridDTO getGridById(Integer gridId) {
        Grid grid = gridMapper.selectById(gridId);
        if (grid == null) {
            throw new RuntimeException("网格不存在");
        }
        return convertToGridDTO(grid);
    }

    @Override
    public IPage<GridDTO> listGrids(Integer provinceId, Integer cityId, Integer status, Integer pageNum, Integer pageSize) {
        Page<Grid> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Grid> query = new LambdaQueryWrapper<>();
        
        if (provinceId != null) {
            query.eq(Grid::getProvinceId, provinceId);
            if (cityId != null) {
                query.eq(Grid::getCityId, cityId);
            }
        }
        if (status != null) {
            query.eq(Grid::getStatus, status);
        }
        query.orderByDesc(Grid::getCreatedAt);
        
        IPage<Grid> gridPage = gridMapper.selectPage(page, query);
        Page<GridDTO> resultPage = new Page<>(gridPage.getCurrent(), gridPage.getSize(), gridPage.getTotal());
        resultPage.setRecords(gridPage.getRecords().stream().map(this::convertToGridDTO).collect(Collectors.toList()));
        return resultPage;
    }

    @Override
    @Transactional
    public GridDTO updateGrid(Integer gridId, GridCreateRequest request) {
        Grid grid = gridMapper.selectById(gridId);
        if (grid == null) {
            throw new RuntimeException("网格不存在");
        }
        
        if (request.getGridCode() != null) {
            grid.setGridCode(request.getGridCode());
        }
        if (request.getGridName() != null) {
            grid.setGridName(request.getGridName());
        }
        if (request.getProvinceId() != null) {
            grid.setProvinceId(request.getProvinceId());
        }
        if (request.getCityId() != null) {
            grid.setCityId(request.getCityId());
        }
        if (request.getDistrict() != null) {
            grid.setDistrict(request.getDistrict());
        }
        if (request.getAddress() != null) {
            grid.setAddress(request.getAddress());
        }
        if (request.getStatus() != null) {
            grid.setStatus(request.getStatus());
        }
        
        gridMapper.updateById(grid);
        return convertToGridDTO(grid);
    }

    @Override
    @Transactional
    public void deleteGrid(Integer gridId) {
        if (gridMapper.selectById(gridId) == null) {
            throw new RuntimeException("网格不存在");
        }
        gridMapper.deleteById(gridId);
    }

    @Override
    public IPage<ReportDetailDTO> listReports(String status, String province, String city, Integer pageNum, Integer pageSize) {
        Page<AqiReport> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<AqiReport> query = new LambdaQueryWrapper<>();
        
        if (status != null && !status.isEmpty()) {
            query.eq(AqiReport::getStatus, status);
        } else if (province != null && !province.isEmpty()) {
            query.eq(AqiReport::getProvince, province);
            if (city != null && !city.isEmpty()) {
                query.eq(AqiReport::getCity, city);
            }
        }
        query.orderByDesc(AqiReport::getReportedAt);
        
        IPage<AqiReport> reportPage = reportMapper.selectPage(page, query);
        Page<ReportDetailDTO> resultPage = new Page<>(reportPage.getCurrent(), reportPage.getSize(), reportPage.getTotal());
        resultPage.setRecords(reportPage.getRecords().stream().map(this::convertToReportDTO).collect(Collectors.toList()));
        return resultPage;
    }

    @Override
    public ReportDetailDTO getReportById(Integer reportId) {
        AqiReport report = reportMapper.selectById(reportId);
        if (report == null) {
            throw new RuntimeException("上报不存在");
        }
        return convertToReportDTO(report);
    }

    @Override
    public IPage<TaskAssignmentDTO> listTasks(Integer userId, String status, Integer pageNum, Integer pageSize) {
        Page<TaskAssignment> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<TaskAssignment> query = new LambdaQueryWrapper<>();
        
        if (userId != null) {
            query.eq(TaskAssignment::getUserId, userId);
        } else if (status != null && !status.isEmpty()) {
            query.eq(TaskAssignment::getStatus, status);
        }
        query.orderByDesc(TaskAssignment::getAssignedAt);
        
        IPage<TaskAssignment> taskPage = taskAssignmentMapper.selectPage(page, query);
        Page<TaskAssignmentDTO> resultPage = new Page<>(taskPage.getCurrent(), taskPage.getSize(), taskPage.getTotal());
        resultPage.setRecords(taskPage.getRecords().stream().map(task -> {
            User admin = userMapper.selectById(task.getAdminId());
            User user = userMapper.selectById(task.getUserId());
            AqiReport report = reportMapper.selectById(task.getReportId());
            return convertToTaskDTO(task, 
                    admin != null ? admin.getRealName() : null, 
                    user != null ? user.getRealName() : null,
                    report != null ? report.getReportCode() : null);
        }).collect(Collectors.toList()));
        return resultPage;
    }

    @Override
    @Transactional
    public void auditDetectData(Integer detectId, AuditRequest request) {
        DetectData detectData = detectDataMapper.selectById(detectId);
        if (detectData == null) {
            throw new RuntimeException("检测数据不存在");
        }

        Integer auditStatus = request.getStatus();
        if (auditStatus != 1 && auditStatus != 2) {
            throw new RuntimeException("审核结果无效，必须为1(合理)或2(不合理)");
        }

        detectData.setStatus(auditStatus);
        detectData.setConfirmNote(request.getRemark());
        detectData.setConfirmedAt(LocalDateTime.now());
        detectDataMapper.updateById(detectData);

        TaskAssignment task = taskAssignmentMapper.selectById(detectData.getTaskId());
        if (task != null) {
            AqiReport report = reportMapper.selectById(task.getReportId());
            if (report != null) {
                if (auditStatus == 1) {
                    report.setStatus("confirmed");
                } else {
                    report.setStatus("unconfirmed");
                }
                report.setConfirmedAt(LocalDateTime.now());
                reportMapper.updateById(report);
            }
        }
    }

    @Override
    public IPage<DetectDataDTO> listDetectData(Integer userId, Integer status, Integer pageNum, Integer pageSize) {
        Page<DetectData> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<DetectData> query = new LambdaQueryWrapper<>();
        
        if (userId != null) {
            query.eq(DetectData::getUserId, userId);
        } else if (status != null) {
            query.eq(DetectData::getStatus, status);
        }
        query.orderByDesc(DetectData::getDetectTime);
        
        IPage<DetectData> detectPage = detectDataMapper.selectPage(page, query);
        Page<DetectDataDTO> resultPage = new Page<>(detectPage.getCurrent(), detectPage.getSize(), detectPage.getTotal());
        resultPage.setRecords(detectPage.getRecords().stream().map(this::convertToDetectDTO).collect(Collectors.toList()));
        return resultPage;
    }

    @Override
    public DetectDataDTO getDetectDataById(Integer detectId) {
        DetectData detectData = detectDataMapper.selectById(detectId);
        if (detectData == null) {
            throw new RuntimeException("检测数据不存在");
        }
        return convertToDetectDTO(detectData);
    }
    
    private DetectDataDTO convertToDetectDTO(DetectData detect) {
        DetectDataDTO dto = new DetectDataDTO();
        dto.setDetectId(detect.getDetectId());
        dto.setTaskId(detect.getTaskId());
        dto.setUserId(detect.getUserId());
        dto.setDetectTime(detect.getDetectTime());
        dto.setAqiValue(detect.getAqiValue());
        dto.setAqiLevel(detect.getAqiLevel());
        dto.setPm25(detect.getPm25());
        dto.setPm10(detect.getPm10());
        dto.setO3(detect.getO3());
        dto.setSo2(detect.getSo2());
        dto.setNo2(detect.getNo2());
        dto.setCo(detect.getCo());
        dto.setLocation(detect.getLocation());
        dto.setPhotos(detect.getPhotos());
        dto.setRemark(detect.getRemark());
        dto.setStatus(detect.getStatus());
        dto.setStatusDesc(switch (detect.getStatus()) {
            case 0 -> "待审核";
            case 1 -> "合理";
            case 2 -> "不合理";
            default -> "未知";
        });
        dto.setConfirmNote(detect.getConfirmNote());
        dto.setConfirmedAt(detect.getConfirmedAt());
        
        User user = userMapper.selectById(detect.getUserId());
        if (user != null) {
            dto.setUserName(user.getRealName());
        }
        
        TaskAssignment task = taskAssignmentMapper.selectById(detect.getTaskId());
        if (task != null) {
            AqiReport report = reportMapper.selectById(task.getReportId());
            if (report != null) {
                dto.setReportCode(report.getReportCode());
            }
        }
        
        return dto;
    }

    private UserDTO convertToUserDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setRealName(user.getRealName());
        dto.setPhone(user.getPhone());
        dto.setRoleId(user.getRoleId());
        dto.setGridId(user.getGridId());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setStatus(user.getStatus());
        dto.setStatusDesc(user.getStatus() == 1 ? "正常" : "禁用");
        dto.setLastLoginAt(user.getLastLoginAt());
        dto.setCreatedAt(user.getCreatedAt());
        
        Role role = roleMapper.selectById(user.getRoleId());
        if (role != null) {
            dto.setRoleName(role.getRoleName());
        }
        Grid grid = gridMapper.selectById(user.getGridId());
        if (grid != null) {
            dto.setGridName(grid.getGridName());
        }
        
        return dto;
    }

    private GridDTO convertToGridDTO(Grid grid) {
        GridDTO dto = new GridDTO();
        dto.setGridId(grid.getGridId());
        dto.setGridCode(grid.getGridCode());
        dto.setGridName(grid.getGridName());
        dto.setProvinceId(grid.getProvinceId());
        dto.setCityId(grid.getCityId());
        dto.setDistrict(grid.getDistrict());
        dto.setAddress(grid.getAddress());
        dto.setStatus(grid.getStatus());
        dto.setStatusDesc(grid.getStatus() == 1 ? "启用" : "禁用");
        dto.setCreatedAt(grid.getCreatedAt());

        if (grid.getProvinceId() != null) {
            Province province = provinceMapper.selectById(grid.getProvinceId());
            if (province != null) {
                dto.setProvince(province.getProvinceName());
            }
        }

        if (grid.getCityId() != null) {
            City city = cityMapper.selectById(grid.getCityId());
            if (city != null) {
                dto.setCity(city.getCityName());
            }
        }

        return dto;
    }

    private ReportDetailDTO convertToReportDTO(AqiReport report) {
        ReportDetailDTO dto = new ReportDetailDTO();
        dto.setReportId(report.getReportId());
        dto.setReportCode(report.getReportCode());
        dto.setUserId(report.getUserId());
        dto.setAqiValue(report.getAqiValue());
        dto.setAqiLevel(report.getAqiLevel());
        dto.setProvince(report.getProvince());
        dto.setCity(report.getCity());
        dto.setAddress(report.getAddress());
        dto.setGridId(report.getGridId());
        dto.setStatus(report.getStatus());
        dto.setStatusDesc(getStatusDesc(report.getStatus()));
        dto.setRemark(report.getRemark());
        dto.setPollutionType(report.getPollutionType());
        dto.setPollutionLevel(report.getPollutionLevel());
        dto.setPollutionDesc(report.getPollutionDesc());
        dto.setPm25(report.getPm25());
        dto.setPm10(report.getPm10());
        dto.setO3(report.getO3());
        dto.setSo2(report.getSo2());
        dto.setNo2(report.getNo2());
        dto.setCo(report.getCo());
        dto.setReportedAt(report.getReportedAt());
        dto.setConfirmedAt(report.getConfirmedAt());
        
        User user = userMapper.selectById(report.getUserId());
        if (user != null) {
            dto.setUserName(user.getRealName());
        }
        Grid grid = gridMapper.selectById(report.getGridId());
        if (grid != null) {
            dto.setGridName(grid.getGridName());
        }
        
        return dto;
    }

    private TaskAssignmentDTO convertToTaskDTO(TaskAssignment task, String adminName, String userName, String reportCode) {
        TaskAssignmentDTO dto = new TaskAssignmentDTO();
        dto.setTaskId(task.getTaskId());
        dto.setReportId(task.getReportId());
        dto.setReportCode(reportCode);
        dto.setAdminId(task.getAdminId());
        dto.setAdminName(adminName);
        dto.setUserId(task.getUserId());
        dto.setUserName(userName);
        dto.setAssignTime(task.getAssignedAt());
        dto.setDeadline(task.getCompletedAt());
        dto.setStatus(task.getStatus());
        dto.setStatusDesc(getTaskStatusDesc(task.getStatus()));
        dto.setRemark(task.getRemark());
        return dto;
    }

    private String getStatusDesc(String status) {
        return switch (status) {
            case "pending" -> "待指派";
            case "assigned" -> "已指派";
            case "detected" -> "已检测";
            case "confirmed" -> "已确认";
            case "unconfirmed" -> "不合理";
            default -> status;
        };
    }

    private String getTaskStatusDesc(String status) {
        return switch (status) {
            case "assigned" -> "已指派";
            case "completed" -> "已完成";
            default -> status;
        };
    }

    @Override
    public List<UserDTO> getLocalGridWorkers(Integer reportId) {
        AqiReport report = reportMapper.selectById(reportId);
        if (report == null) {
            throw new RuntimeException("上报不存在");
        }
        
        String cityName = report.getCity();
        if (cityName == null || cityName.isEmpty()) {
            throw new RuntimeException("上报记录缺少城市信息");
        }
        
        List<User> workers = userMapper.findGridWorkersByCity(cityName);
        return workers.stream().map(this::convertToUserDTO).collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getCrossCityGridWorkers(Integer reportId) {
        AqiReport report = reportMapper.selectById(reportId);
        if (report == null) {
            throw new RuntimeException("上报不存在");
        }
        
        String cityName = report.getCity();
        if (cityName == null || cityName.isEmpty()) {
            throw new RuntimeException("上报记录缺少城市信息");
        }
        
        List<User> workers = userMapper.findGridWorkersNotInCity(cityName);
        return workers.stream().map(this::convertToUserDTO).collect(Collectors.toList());
    }

    @Override
    public boolean hasLocalGridWorkers(Integer reportId) {
        AqiReport report = reportMapper.selectById(reportId);
        if (report == null) {
            throw new RuntimeException("上报不存在");
        }
        
        String cityName = report.getCity();
        if (cityName == null || cityName.isEmpty()) {
            return false;
        }
        
        int count = userMapper.countGridWorkersByCity(cityName);
        return count > 0;
    }

    @Override
    @Transactional
    public TaskAssignmentDTO assignTask(TaskAssignRequest request) {
        AqiReport report = reportMapper.selectById(request.getReportId());
        if (report == null) {
            throw new RuntimeException("上报不存在");
        }
        
        if (!"pending".equals(report.getStatus())) {
            throw new RuntimeException("该上报已被指派或处理中");
        }

        User admin = userMapper.selectById(request.getAdminId());
        if (admin == null) {
            throw new RuntimeException("管理员不存在");
        }
        
        User user = userMapper.selectById(request.getUserId());
        if (user == null) {
            throw new RuntimeException("网格员不存在");
        }

        if (user.getRoleId() != 2) {
            throw new RuntimeException("所选用户不是网格员");
        }

        String reportCity = report.getCity();
        String userCity = null;
        
        if (user.getGridId() != null) {
            Grid grid = gridMapper.selectById(user.getGridId());
            if (grid != null && grid.getCityId() != null) {
                City city = cityMapper.selectById(grid.getCityId());
                if (city != null) {
                    userCity = city.getCityName();
                }
            }
        }

        boolean isLocalAssignment = reportCity != null && userCity != null && reportCity.equals(userCity);
        
        if (!isLocalAssignment) {
            int localWorkerCount = userMapper.countGridWorkersByCity(reportCity);
            if (localWorkerCount > 0) {
                throw new RuntimeException("本市存在可用网格员，请优先指派本市人员");
            }
        }

        TaskAssignment task = new TaskAssignment();
        task.setTaskCode(generateTaskCode());
        task.setReportId(request.getReportId());
        task.setAdminId(request.getAdminId());
        task.setUserId(request.getUserId());
        task.setStatus("assigned");
        task.setAssignedAt(LocalDateTime.now());
        
        if (request.getDeadline() != null) {
            task.setCompletedAt(request.getDeadline());
        }
        
        taskAssignmentMapper.insert(task);
        
        report.setStatus("assigned");
        reportMapper.updateById(report);
        
        return convertToTaskDTO(task, admin.getRealName(), user.getRealName(), report.getReportCode());
    }

    private String generateTaskCode() {
        return "TSK-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 1000);
    }
}