package com.neusoft.env.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.env.entity.UserPoints;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserPointsMapper extends BaseMapper<UserPoints> {

    @Update("UPDATE user_points SET total_points = total_points + #{points} WHERE user_id = #{userId}")
    int addPoints(@Param("userId") Integer userId, @Param("points") Integer points);
}