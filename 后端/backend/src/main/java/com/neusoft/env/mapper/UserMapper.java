package com.neusoft.env.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.env.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT * FROM users WHERE username = #{username}")
    User findByUsername(@Param("username") String username);

    @Select("SELECT * FROM users WHERE phone = #{phone}")
    User findByPhone(@Param("phone") String phone);

    @Select("SELECT * FROM users WHERE role_id = #{roleId}")
    List<User> findByRoleId(@Param("roleId") Integer roleId);

    @Select("SELECT * FROM users WHERE status = #{status}")
    List<User> findByStatus(@Param("status") Integer status);

    @Select("SELECT * FROM users WHERE grid_id = #{gridId}")
    List<User> findByGridId(@Param("gridId") Integer gridId);

    @Select("SELECT * FROM users WHERE username LIKE CONCAT('%', #{username}, '%')")
    IPage<User> findByUsernameContaining(Page<User> page, @Param("username") String username);

    @Select("SELECT COUNT(*) > 0 FROM users WHERE username = #{username}")
    boolean existsByUsername(@Param("username") String username);

    @Select("SELECT COUNT(*) > 0 FROM users WHERE phone = #{phone}")
    boolean existsByPhone(@Param("phone") String phone);

    @Select("SELECT u.* FROM users u " +
            "LEFT JOIN grids g ON u.grid_id = g.grid_id " +
            "LEFT JOIN cities c ON g.city_id = c.city_id " +
            "WHERE u.role_id = 2 AND u.status = 1 AND c.city_name = #{cityName}")
    List<User> findGridWorkersByCity(@Param("cityName") String cityName);

    @Select("SELECT u.* FROM users u " +
            "LEFT JOIN grids g ON u.grid_id = g.grid_id " +
            "LEFT JOIN cities c ON g.city_id = c.city_id " +
            "LEFT JOIN provinces p ON g.province_id = p.province_id " +
            "WHERE u.role_id = 2 AND u.status = 1 AND c.city_name != #{cityName}")
    List<User> findGridWorkersNotInCity(@Param("cityName") String cityName);

    @Select("SELECT COUNT(*) FROM users u " +
            "LEFT JOIN grids g ON u.grid_id = g.grid_id " +
            "LEFT JOIN cities c ON g.city_id = c.city_id " +
            "WHERE u.role_id = 2 AND u.status = 1 AND c.city_name = #{cityName}")
    int countGridWorkersByCity(@Param("cityName") String cityName);

    default IPage<User> findByRoleId(Page<User> page, Integer roleId) {
        return selectPage(page, new LambdaQueryWrapper<User>().eq(User::getRoleId, roleId));
    }

    default IPage<User> findByStatus(Page<User> page, Integer status) {
        return selectPage(page, new LambdaQueryWrapper<User>().eq(User::getStatus, status));
    }
}