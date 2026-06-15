package com.neusoft.env.service.region;

import com.neusoft.env.entity.City;
import com.neusoft.env.entity.Province;

import java.util.List;

public interface RegionService {

    List<Province> getAllProvinces();

    List<City> getCitiesByProvinceId(Integer provinceId);

    List<City> getAllCities();
}