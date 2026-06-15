package com.neusoft.env.service.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.neusoft.env.dto.request.*;
import com.neusoft.env.dto.response.*;

public interface AdminService {

    UserDTO createUser(UserCreateRequest request);

    UserDTO getUserById(Integer userId);

    IPage<UserDTO> listUsers(String username, Integer roleId, Integer status, Integer pageNum, Integer pageSize);

    UserDTO updateUser(Integer userId, UserUpdateRequest request);

    void deleteUser(Integer userId);

    GridDTO createGrid(GridCreateRequest request);

    GridDTO getGridById(Integer gridId);

    IPage<GridDTO> listGrids(Integer provinceId, Integer cityId, Integer status, Integer pageNum, Integer pageSize);

    GridDTO updateGrid(Integer gridId, GridCreateRequest request);

    void deleteGrid(Integer gridId);

    IPage<ReportDetailDTO> listReports(String status, String province, String city, Integer pageNum, Integer pageSize);

    ReportDetailDTO getReportById(Integer reportId);

    TaskAssignmentDTO assignTask(TaskAssignRequest request);

    IPage<TaskAssignmentDTO> listTasks(Integer workerId, String status, Integer pageNum, Integer pageSize);

    void auditDetectData(Integer detectId, AuditRequest request);

    IPage<DetectDataDTO> listDetectData(Integer userId, Integer status, Integer pageNum, Integer pageSize);

    DetectDataDTO getDetectDataById(Integer detectId);

    java.util.List<UserDTO> getLocalGridWorkers(Integer reportId);

    java.util.List<UserDTO> getCrossCityGridWorkers(Integer reportId);

    boolean hasLocalGridWorkers(Integer reportId);
}