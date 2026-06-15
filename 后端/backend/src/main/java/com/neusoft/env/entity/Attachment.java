package com.neusoft.env.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("attachments")
public class Attachment {

    @TableId(value = "attach_id", type = IdType.AUTO)
    private Integer attachId;

    private Integer reportId;

    private Integer detectId;

    private String fileUrl;

    private String fileType = "image";

    private String uploadType;

    private LocalDateTime uploadedAt;
}