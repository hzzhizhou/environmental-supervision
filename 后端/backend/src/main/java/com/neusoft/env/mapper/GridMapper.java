package com.neusoft.env.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.env.entity.Grid;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GridMapper extends BaseMapper<Grid> {

    @Select("SELECT * FROM grids WHERE province_id = #{provinceId} AND city_id = #{cityId}")
    List<Grid> findByProvinceAndCity(@Param("provinceId") Integer provinceId, @Param("cityId") Integer cityId);

    @Select("SELECT * FROM grids WHERE province_id = #{provinceId}")
    IPage<Grid> findByProvince(Page<Grid> page, @Param("provinceId") Integer provinceId);

    @Select("SELECT * FROM grids WHERE province_id = #{provinceId} AND city_id = #{cityId}")
    IPage<Grid> findByProvinceAndCity(Page<Grid> page, @Param("provinceId") Integer provinceId, @Param("cityId") Integer cityId);

    @Select("SELECT * FROM grids WHERE status = #{status}")
    IPage<Grid> findByStatus(Page<Grid> page, @Param("status") Integer status);

    default IPage<Grid> findByProvinceAndCityPage(Page<Grid> page, Integer provinceId, Integer cityId) {
        LambdaQueryWrapper<Grid> query = new LambdaQueryWrapper<>();
        query.eq(Grid::getProvinceId, provinceId);
        if (cityId != null) {
            query.eq(Grid::getCityId, cityId);
        }
        return selectPage(page, query);
    }
}