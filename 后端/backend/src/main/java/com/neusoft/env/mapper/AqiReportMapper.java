package com.neusoft.env.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.env.entity.AqiReport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AqiReportMapper extends BaseMapper<AqiReport> {

    @Select("SELECT COUNT(*) FROM aqi_reports")
    long countAll();

    @Select("SELECT COUNT(*) FROM aqi_reports WHERE status = #{status}")
    long countByStatus(@Param("status") String status);

    @Select("SELECT * FROM aqi_reports WHERE status = #{status}")
    IPage<AqiReport> findByStatus(Page<AqiReport> page, @Param("status") String status);

    @Select("SELECT * FROM aqi_reports WHERE province = #{province}")
    IPage<AqiReport> findByProvince(Page<AqiReport> page, @Param("province") String province);

    @Select("SELECT * FROM aqi_reports WHERE province = #{province} AND city = #{city}")
    IPage<AqiReport> findByProvinceAndCity(Page<AqiReport> page, @Param("province") String province, @Param("city") String city);

    @Select("SELECT * FROM aqi_reports WHERE user_id = #{userId}")
    IPage<AqiReport> findByUserId(Page<AqiReport> page, @Param("userId") Integer userId);

    @Select("SELECT * FROM aqi_reports WHERE user_id = #{userId}")
    List<AqiReport> findByUserId(@Param("userId") Integer userId);

    default IPage<AqiReport> findByCondition(Page<AqiReport> page, Integer userId, String status, 
                                              String province, String city, String keyword) {
        LambdaQueryWrapper<AqiReport> query = new LambdaQueryWrapper<>();
        if (userId != null) query.eq(AqiReport::getUserId, userId);
        if (status != null && !status.isEmpty()) query.eq(AqiReport::getStatus, status);
        if (province != null && !province.isEmpty()) query.eq(AqiReport::getProvince, province);
        if (city != null && !city.isEmpty()) query.eq(AqiReport::getCity, city);
        if (keyword != null && !keyword.isEmpty()) {
            query.like(AqiReport::getAddress, keyword).or().like(AqiReport::getRemark, keyword);
        }
        query.orderByDesc(AqiReport::getReportedAt);
        return selectPage(page, query);
    }
}