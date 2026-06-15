package com.neusoft.env.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.env.entity.City;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CityMapper extends BaseMapper<City> {

    @Select("SELECT city_id as cityId, city_code as cityCode, city_name as cityName, province_id as provinceId FROM cities WHERE province_id = #{provinceId} AND status = 1 ORDER BY city_id")
    List<City> selectCitiesByProvinceId(Integer provinceId);

    @Select("SELECT city_id as cityId, city_code as cityCode, city_name as cityName, province_id as provinceId FROM cities WHERE status = 1 ORDER BY province_id, city_id")
    List<City> selectAllCities();
}