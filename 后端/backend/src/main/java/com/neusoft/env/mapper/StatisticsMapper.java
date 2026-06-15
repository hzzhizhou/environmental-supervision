package com.neusoft.env.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.env.entity.Statistics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StatisticsMapper extends BaseMapper<Statistics> {

    @Select("SELECT * FROM statistics WHERE stat_month = #{statMonth}")
    Statistics findFirstByStatMonth(@Param("statMonth") String statMonth);

    @Select("SELECT * FROM statistics WHERE stat_month = #{statMonth}")
    List<Statistics> findByStatMonth(@Param("statMonth") String statMonth);

    @Select("SELECT * FROM statistics WHERE province = #{province}")
    List<Statistics> findByProvince(@Param("province") String province);

    @Select("SELECT * FROM statistics WHERE province = #{province} AND stat_month = #{statMonth}")
    List<Statistics> findByProvinceAndStatMonth(@Param("province") String province, @Param("statMonth") String statMonth);

    @Select("SELECT * FROM statistics WHERE province = #{province} AND city = #{city} AND stat_month = #{statMonth}")
    List<Statistics> findByProvinceAndCityAndStatMonth(@Param("province") String province, 
                                                       @Param("city") String city, 
                                                       @Param("statMonth") String statMonth);

    @Select("SELECT COUNT(*) > 0 FROM statistics WHERE province = #{province} AND city = #{city} AND stat_month = #{statMonth}")
    boolean existsByProvinceAndCityAndStatMonth(@Param("province") String province, 
                                                 @Param("city") String city, 
                                                 @Param("statMonth") String statMonth);
}