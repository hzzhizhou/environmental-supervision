package com.neusoft.env.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.env.entity.TaskAssignment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TaskAssignmentMapper extends BaseMapper<TaskAssignment> {

    @Select("SELECT * FROM task_assignments WHERE worker_id = #{userId}")
    IPage<TaskAssignment> findByWorkerId(Page<TaskAssignment> page, @Param("userId") Integer userId);

    @Select("SELECT * FROM task_assignments WHERE status = #{status}")
    IPage<TaskAssignment> findByStatus(Page<TaskAssignment> page, @Param("status") String status);

    @Select("SELECT * FROM task_assignments WHERE worker_id = #{userId} AND status = #{status}")
    IPage<TaskAssignment> findByWorkerIdAndStatus(Page<TaskAssignment> page, 
                                                   @Param("userId") Integer userId, 
                                                   @Param("status") String status);

    @Select("SELECT * FROM task_assignments WHERE report_id = #{reportId}")
    TaskAssignment findByReportId(@Param("reportId") Integer reportId);

    default IPage<TaskAssignment> findByWorkerIdWithStatus(Page<TaskAssignment> page, Integer userId, String status) {
        LambdaQueryWrapper<TaskAssignment> query = new LambdaQueryWrapper<>();
        query.eq(TaskAssignment::getUserId, userId);
        if (status != null && !status.isEmpty()) {
            query.eq(TaskAssignment::getStatus, status);
        }
        query.orderByDesc(TaskAssignment::getAssignedAt);
        return selectPage(page, query);
    }
}