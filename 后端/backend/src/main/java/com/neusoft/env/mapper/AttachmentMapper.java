package com.neusoft.env.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.env.entity.Attachment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AttachmentMapper extends BaseMapper<Attachment> {

    @Select("SELECT * FROM attachments WHERE report_id = #{reportId}")
    List<Attachment> findByReportId(@Param("reportId") Integer reportId);

    @Select("DELETE FROM attachments WHERE report_id = #{reportId}")
    void deleteByReportId(@Param("reportId") Integer reportId);
}