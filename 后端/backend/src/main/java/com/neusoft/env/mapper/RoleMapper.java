package com.neusoft.env.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.env.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    @Select("SELECT * FROM roles WHERE role_id = #{roleId}")
    Role findByRoleId(@Param("roleId") Integer roleId);
}