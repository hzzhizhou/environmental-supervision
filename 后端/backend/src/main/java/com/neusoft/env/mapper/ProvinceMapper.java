package com.neusoft.env.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.env.entity.Province;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProvinceMapper extends BaseMapper<Province> {

    @Select("SELECT province_id as provinceId, province_code as provinceCode, province_name as provinceName FROM provinces WHERE status = 1 ORDER BY province_id")
    List<Province> selectAllProvinces();
}