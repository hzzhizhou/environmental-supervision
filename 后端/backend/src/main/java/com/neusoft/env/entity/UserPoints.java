package com.neusoft.env.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_points")
public class UserPoints {

    @TableId("user_id")
    private Integer userId;

    @TableField("total_points")
    private Integer totalPoints;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}