package com.neusoft.env.service.region.impl;

import com.neusoft.env.entity.City;
import com.neusoft.env.entity.Province;
import com.neusoft.env.mapper.CityMapper;
import com.neusoft.env.mapper.ProvinceMapper;
import com.neusoft.env.service.region.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegionServiceImpl implements RegionService {

    @Autowired
    private ProvinceMapper provinceMapper;

    @Autowired
    private CityMapper cityMapper;

    @Override
    public List<Province> getAllProvinces() {
        return provinceMapper.selectAllProvinces();
    }

    @Override
    public List<City> getCitiesByProvinceId(Integer provinceId) {
        return cityMapper.selectCitiesByProvinceId(provinceId);
    }

    @Override
    public List<City> getAllCities() {
        return cityMapper.selectAllCities();
    }
}