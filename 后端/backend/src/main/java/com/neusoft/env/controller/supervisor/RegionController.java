package com.neusoft.env.controller.supervisor;

import com.neusoft.env.dto.response.ApiResponse;
import com.neusoft.env.entity.City;
import com.neusoft.env.entity.Province;
import com.neusoft.env.service.region.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supervisor/regions")
public class RegionController {

    @Autowired
    private RegionService regionService;

    @GetMapping("/provinces")
    public ApiResponse<List<Province>> getAllProvinces() {
        List<Province> provinces = regionService.getAllProvinces();
        return ApiResponse.success(provinces);
    }

    @GetMapping("/cities")
    public ApiResponse<List<City>> getCitiesByProvinceId(@RequestParam Integer provinceId) {
        List<City> cities = regionService.getCitiesByProvinceId(provinceId);
        return ApiResponse.success(cities);
    }
}