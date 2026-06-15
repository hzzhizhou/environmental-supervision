package com.neusoft.env.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.neusoft.env.dto.request.*;
import com.neusoft.env.dto.response.*;
import com.neusoft.env.entity.City;
import com.neusoft.env.entity.Province;
import com.neusoft.env.service.admin.AdminService;
import com.neusoft.env.service.region.RegionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private RegionService regionService;

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<IPage<UserDTO>>> listUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Integer roleId,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        
        IPage<UserDTO> users = adminService.listUsers(username, roleId, status, pageNum, pageSize);
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable Integer userId) {
        UserDTO user = adminService.getUserById(userId);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @PostMapping("/users")
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@Valid @RequestBody UserCreateRequest request) {
        UserDTO user = adminService.createUser(request);
        return ResponseEntity.ok(ApiResponse.success("用户创建成功", user));
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(
            @PathVariable Integer userId,
            @RequestBody UserUpdateRequest request) {
        UserDTO user = adminService.updateUser(userId, request);
        return ResponseEntity.ok(ApiResponse.success("用户更新成功", user));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Integer userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.ok(ApiResponse.success("用户删除成功", null));
    }

    @GetMapping("/grids")
    public ResponseEntity<ApiResponse<IPage<GridDTO>>> listGrids(
            @RequestParam(required = false) Integer provinceId,
            @RequestParam(required = false) Integer cityId,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        
        IPage<GridDTO> grids = adminService.listGrids(provinceId, cityId, status, pageNum, pageSize);
        return ResponseEntity.ok(ApiResponse.success(grids));
    }

    @GetMapping("/grids/{gridId}")
    public ResponseEntity<ApiResponse<GridDTO>> getGridById(@PathVariable Integer gridId) {
        GridDTO grid = adminService.getGridById(gridId);
        return ResponseEntity.ok(ApiResponse.success(grid));
    }

    @PostMapping("/grids")
    public ResponseEntity<ApiResponse<GridDTO>> createGrid(@Valid @RequestBody GridCreateRequest request) {
        GridDTO grid = adminService.createGrid(request);
        return ResponseEntity.ok(ApiResponse.success("网格创建成功", grid));
    }

    @PutMapping("/grids/{gridId}")
    public ResponseEntity<ApiResponse<GridDTO>> updateGrid(
            @PathVariable Integer gridId,
            @RequestBody GridCreateRequest request) {
        GridDTO grid = adminService.updateGrid(gridId, request);
        return ResponseEntity.ok(ApiResponse.success("网格更新成功", grid));
    }

    @DeleteMapping("/grids/{gridId}")
    public ResponseEntity<ApiResponse<Void>> deleteGrid(@PathVariable Integer gridId) {
        adminService.deleteGrid(gridId);
        return ResponseEntity.ok(ApiResponse.success("网格删除成功", null));
    }

    @GetMapping("/reports")
    public ResponseEntity<ApiResponse<IPage<ReportDetailDTO>>> listReports(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String province,
            @RequestParam(required = false) String city,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        
        IPage<ReportDetailDTO> reports = adminService.listReports(status, province, city, pageNum, pageSize);
        return ResponseEntity.ok(ApiResponse.success(reports));
    }

    @GetMapping("/reports/{reportId}")
    public ResponseEntity<ApiResponse<ReportDetailDTO>> getReportById(@PathVariable Integer reportId) {
        ReportDetailDTO report = adminService.getReportById(reportId);
        return ResponseEntity.ok(ApiResponse.success(report));
    }

    @PostMapping("/tasks/assign")
    public ResponseEntity<ApiResponse<TaskAssignmentDTO>> assignTask(@Valid @RequestBody TaskAssignRequest request) {
        TaskAssignmentDTO task = adminService.assignTask(request);
        return ResponseEntity.ok(ApiResponse.success("指派成功", task));
    }

    @GetMapping("/tasks")
    public ResponseEntity<ApiResponse<IPage<TaskAssignmentDTO>>> listTasks(
            @RequestParam(required = false) Integer workerId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        
        IPage<TaskAssignmentDTO> tasks = adminService.listTasks(workerId, status, pageNum, pageSize);
        return ResponseEntity.ok(ApiResponse.success(tasks));
    }

    @GetMapping("/detect-data")
    public ResponseEntity<ApiResponse<IPage<DetectDataDTO>>> listDetectData(
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        
        IPage<DetectDataDTO> data = adminService.listDetectData(userId, status, pageNum, pageSize);
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    @GetMapping("/detect-data/{detectId}")
    public ResponseEntity<ApiResponse<DetectDataDTO>> getDetectDataById(@PathVariable Integer detectId) {
        DetectDataDTO detectData = adminService.getDetectDataById(detectId);
        return ResponseEntity.ok(ApiResponse.success(detectData));
    }

    @PutMapping("/audit/{detectId}")
    public ResponseEntity<ApiResponse<Void>> auditDetectData(
            @PathVariable Integer detectId,
            @Valid @RequestBody AuditRequest request) {
        adminService.auditDetectData(detectId, request);
        return ResponseEntity.ok(ApiResponse.success("审核完成", null));
    }

    @GetMapping("/workers/local")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getLocalGridWorkers(@RequestParam Integer reportId) {
        List<UserDTO> workers = adminService.getLocalGridWorkers(reportId);
        return ResponseEntity.ok(ApiResponse.success(workers));
    }

    @GetMapping("/workers/cross-city")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getCrossCityGridWorkers(@RequestParam Integer reportId) {
        List<UserDTO> workers = adminService.getCrossCityGridWorkers(reportId);
        return ResponseEntity.ok(ApiResponse.success(workers));
    }

    @GetMapping("/workers/local/check")
    public ResponseEntity<ApiResponse<Boolean>> checkLocalGridWorkers(@RequestParam Integer reportId) {
        boolean hasLocal = adminService.hasLocalGridWorkers(reportId);
        return ResponseEntity.ok(ApiResponse.success(hasLocal));
    }

    @GetMapping("/provinces")
    public ResponseEntity<ApiResponse<List<Province>>> getProvinces() {
        List<Province> provinces = regionService.getAllProvinces();
        return ResponseEntity.ok(ApiResponse.success(provinces));
    }

    @GetMapping("/cities")
    public ResponseEntity<ApiResponse<List<City>>> getCities(
            @RequestParam(required = false) Integer provinceId) {
        List<City> cities;
        if (provinceId != null) {
            cities = regionService.getCitiesByProvinceId(provinceId);
        } else {
            cities = regionService.getAllCities();
        }
        return ResponseEntity.ok(ApiResponse.success(cities));
    }
}