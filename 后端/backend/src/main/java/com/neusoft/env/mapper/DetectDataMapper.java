package com.neusoft.env.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.env.entity.DetectData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DetectDataMapper extends BaseMapper<DetectData> {

    @Select("SELECT * FROM detect_data WHERE worker_id = #{userId}")
    IPage<DetectData> findByWorkerId(Page<DetectData> page, @Param("userId") Integer userId);

    @Select("SELECT * FROM detect_data WHERE status = #{status}")
    IPage<DetectData> findByStatus(Page<DetectData> page, @Param("status") Integer status);

    @Select("SELECT * FROM detect_data WHERE worker_id = #{userId} AND status = #{status}")
    IPage<DetectData> findByWorkerIdAndStatus(Page<DetectData> page, 
                                               @Param("userId") Integer userId, 
                                               @Param("status") Integer status);

    @Select("SELECT * FROM detect_data WHERE task_id = #{taskId}")
    DetectData findByTaskId(@Param("taskId") Integer taskId);

    default IPage<DetectData> findByWorkerIdWithStatus(Page<DetectData> page, Integer userId, Integer status) {
        LambdaQueryWrapper<DetectData> query = new LambdaQueryWrapper<>();
        query.eq(DetectData::getUserId, userId);
        if (status != null) {
            query.eq(DetectData::getStatus, status);
        }
        query.orderByDesc(DetectData::getDetectTime);
        return selectPage(page, query);
    }
}