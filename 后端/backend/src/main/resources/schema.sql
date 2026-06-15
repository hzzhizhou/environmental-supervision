SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci;
SET CHARACTER SET utf8mb4;

CREATE DATABASE IF NOT EXISTS epm_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE epm_db;

CREATE TABLE `roles` (
  `role_id` INT NOT NULL AUTO_INCREMENT COMMENT '角色唯一标识，主键',
  `role_name` VARCHAR(20) NOT NULL COMMENT '角色名称',
  `role_code` VARCHAR(20) NOT NULL COMMENT '角色代码',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

CREATE TABLE `provinces` (
  `province_id` INT NOT NULL AUTO_INCREMENT COMMENT '省份唯一标识，主键',
  `province_code` VARCHAR(10) NOT NULL COMMENT '省份编码',
  `province_name` VARCHAR(30) NOT NULL COMMENT '省份名称',
  `status` TINYINT DEFAULT 1 COMMENT '状态',
  PRIMARY KEY (`province_id`),
  UNIQUE KEY `uk_province_code` (`province_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='省份表';

CREATE TABLE `cities` (
  `city_id` INT NOT NULL AUTO_INCREMENT COMMENT '城市唯一标识，主键',
  `city_code` VARCHAR(10) NOT NULL COMMENT '城市编码',
  `city_name` VARCHAR(30) NOT NULL COMMENT '城市名称',
  `province_id` INT NOT NULL COMMENT '所属省份ID',
  `status` TINYINT DEFAULT 1 COMMENT '状态',
  PRIMARY KEY (`city_id`),
  UNIQUE KEY `uk_city_code` (`city_code`),
  KEY `idx_province_id` (`province_id`),
  FOREIGN KEY (`province_id`) REFERENCES `provinces` (`province_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='城市表';

CREATE TABLE `grids` (
  `grid_id` INT NOT NULL AUTO_INCREMENT COMMENT '网格唯一标识，主键',
  `grid_code` VARCHAR(30) NOT NULL COMMENT '网格编码',
  `grid_name` VARCHAR(50) NOT NULL COMMENT '网格名称',
  `province_id` INT NOT NULL COMMENT '省份ID，关联provinces表',
  `city_id` INT NOT NULL COMMENT '城市ID，关联cities表',
  `district` VARCHAR(30) DEFAULT NULL COMMENT '区/县名称',
  `address` VARCHAR(200) DEFAULT NULL COMMENT '详细地址',
  `status` TINYINT DEFAULT 1 COMMENT '网格状态',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`grid_id`),
  UNIQUE KEY `uk_grid_code` (`grid_code`),
  FOREIGN KEY (`province_id`) REFERENCES `provinces` (`province_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (`city_id`) REFERENCES `cities` (`city_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='网格表';

CREATE TABLE `users` (
  `user_id` INT NOT NULL AUTO_INCREMENT COMMENT '用户唯一标识，主键',
  `username` VARCHAR(50) NOT NULL COMMENT '登录账号',
  `password` VARCHAR(255) NOT NULL COMMENT '加密后的密码',
  `real_name` VARCHAR(30) DEFAULT NULL COMMENT '真实姓名',
  `phone` VARCHAR(11) NOT NULL COMMENT '手机号码',
  `role_id` INT NOT NULL COMMENT '角色ID',
  `grid_id` INT DEFAULT NULL COMMENT '网格ID',
  `avatar_url` VARCHAR(255) DEFAULT NULL COMMENT '头像图片地址',
  `allow_cross_region` TINYINT DEFAULT 0 COMMENT '是否允许接受异地任务',
  `status` TINYINT DEFAULT 1 COMMENT '账号状态',
  `last_login_at` TIMESTAMP NULL COMMENT '最后登录时间',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '账号注册时间',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_phone` (`phone`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_grid_id` (`grid_id`),
  FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  FOREIGN KEY (`grid_id`) REFERENCES `grids` (`grid_id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

CREATE TABLE `sms_logs` (
  `sms_id` INT NOT NULL AUTO_INCREMENT COMMENT '短信记录唯一标识',
  `phone` VARCHAR(11) NOT NULL COMMENT '接收验证码的手机号',
  `code` VARCHAR(6) NOT NULL COMMENT '6位数字验证码',
  `type` VARCHAR(20) NOT NULL COMMENT '验证码类型',
  `is_used` TINYINT DEFAULT 0 COMMENT '是否已使用',
  `expired_at` TIMESTAMP NOT NULL COMMENT '验证码过期时间',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '验证码发送时间',
  PRIMARY KEY (`sms_id`),
  KEY `idx_phone_code` (`phone`, `code`),
  KEY `idx_expired` (`expired_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='短信验证码记录表';

CREATE TABLE `aqi_reports` (
  `report_id` INT NOT NULL AUTO_INCREMENT COMMENT '上报记录ID',
  `report_code` VARCHAR(30) NOT NULL COMMENT '上报编号',
  `user_id` INT NOT NULL COMMENT '公众监督员ID',
  `aqi_value` INT DEFAULT NULL COMMENT 'AQI数值',
  `aqi_level` VARCHAR(20) DEFAULT NULL COMMENT 'AQI等级',
  `province` VARCHAR(30) NOT NULL COMMENT '省份名称',
  `city` VARCHAR(30) NOT NULL COMMENT '城市名称',
  `address` VARCHAR(200) DEFAULT NULL COMMENT '详细地址',
  `grid_id` INT DEFAULT NULL COMMENT '系统自动匹配的网格ID',
  `status` VARCHAR(20) DEFAULT 'pending' COMMENT '处理状态',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  `pollution_type` VARCHAR(50) DEFAULT NULL COMMENT '污染类型',
  `pollution_level` VARCHAR(20) DEFAULT NULL COMMENT '污染程度',
  `pollution_desc` VARCHAR(500) DEFAULT NULL COMMENT '污染描述',
  `photos` TEXT DEFAULT NULL COMMENT '现场图片URL，多张图片用逗号分隔',
  `pm25` DECIMAL(8,2) DEFAULT NULL COMMENT 'PM2.5浓度',
  `pm10` DECIMAL(8,2) DEFAULT NULL COMMENT 'PM10浓度',
  `o3` DECIMAL(8,2) DEFAULT NULL COMMENT '臭氧浓度',
  `so2` DECIMAL(8,2) DEFAULT NULL COMMENT '二氧化硫浓度',
  `no2` DECIMAL(8,2) DEFAULT NULL COMMENT '二氧化氮浓度',
  `co` DECIMAL(8,2) DEFAULT NULL COMMENT '一氧化碳浓度',
  `reported_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '上报时间',
  `confirmed_at` TIMESTAMP NULL COMMENT '最终确认时间',
  PRIMARY KEY (`report_id`),
  UNIQUE KEY `uk_report_code` (`report_code`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status_grid` (`status`, `grid_id`),
  KEY `idx_reported_at` (`reported_at`),
  FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  FOREIGN KEY (`grid_id`) REFERENCES `grids` (`grid_id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公众AQI上报表';

CREATE TABLE `task_assignments` (
  `task_id` INT NOT NULL AUTO_INCREMENT COMMENT '任务唯一标识',
  `task_code` VARCHAR(30) NOT NULL COMMENT '任务编号',
  `report_id` INT NOT NULL COMMENT '关联的AQI上报记录ID',
  `user_id` INT NOT NULL COMMENT '指派的网格员ID',
  `admin_id` INT DEFAULT NULL COMMENT '指派的管理员ID',
  `status` VARCHAR(20) DEFAULT 'assigned' COMMENT '任务状态：assigned-已指派，completed-已完成',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '任务备注',
  `assigned_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '任务指派时间',
  `completed_at` TIMESTAMP NULL COMMENT '任务完成时间',
  PRIMARY KEY (`task_id`),
  UNIQUE KEY `uk_task_code` (`task_code`),
  KEY `idx_report_id` (`report_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  FOREIGN KEY (`report_id`) REFERENCES `aqi_reports` (`report_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务指派表';

CREATE TABLE `detect_data` (
  `detect_id` INT NOT NULL AUTO_INCREMENT COMMENT '检测数据唯一标识',
  `task_id` INT NOT NULL COMMENT '关联的任务ID',
  `user_id` INT NOT NULL COMMENT '提交检测数据的网格员ID',
  `aqi_value` INT NOT NULL COMMENT '实测AQI数值',
  `aqi_level` VARCHAR(20) NOT NULL COMMENT '实测AQI等级',
  `pm25` DECIMAL(8,2) DEFAULT NULL COMMENT '实测PM2.5浓度',
  `pm10` DECIMAL(8,2) DEFAULT NULL COMMENT '实测PM10浓度',
  `o3` DECIMAL(8,2) DEFAULT NULL COMMENT '实测臭氧浓度',
  `so2` DECIMAL(8,2) DEFAULT NULL COMMENT '实测二氧化硫浓度',
  `no2` DECIMAL(8,2) DEFAULT NULL COMMENT '实测二氧化氮浓度',
  `co` DECIMAL(8,2) DEFAULT NULL COMMENT '实测一氧化碳浓度',
  `location` VARCHAR(200) NOT NULL COMMENT '检测地点',
  `photo_url` VARCHAR(255) DEFAULT NULL COMMENT '现场照片URL',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '检测备注',
  `status` INT DEFAULT 0 COMMENT '审核状态：0-待审核，1-合理，2-不合理',
  `confirm_note` VARCHAR(500) DEFAULT NULL COMMENT '审核备注',
  `detected_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '检测时间',
  `audited_at` TIMESTAMP NULL COMMENT '审核时间',
  PRIMARY KEY (`detect_id`),
  KEY `idx_task_id` (`task_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  FOREIGN KEY (`task_id`) REFERENCES `task_assignments` (`task_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='网格检测数据表';

CREATE TABLE `attachments` (
  `attachment_id` INT NOT NULL AUTO_INCREMENT COMMENT '附件唯一标识',
  `report_id` INT DEFAULT NULL COMMENT '关联的上报记录ID',
  `detect_id` INT DEFAULT NULL COMMENT '关联的检测数据ID',
  `file_name` VARCHAR(100) NOT NULL COMMENT '原始文件名',
  `file_path` VARCHAR(255) NOT NULL COMMENT '文件存储路径',
  `file_size` BIGINT NOT NULL COMMENT '文件大小（字节）',
  `file_type` VARCHAR(50) DEFAULT NULL COMMENT '文件类型',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
  PRIMARY KEY (`attachment_id`),
  KEY `idx_report_id` (`report_id`),
  KEY `idx_detect_id` (`detect_id`),
  FOREIGN KEY (`report_id`) REFERENCES `aqi_reports` (`report_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  FOREIGN KEY (`detect_id`) REFERENCES `detect_data` (`detect_id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='附件表';

CREATE TABLE `audit_logs` (
  `log_id` INT NOT NULL AUTO_INCREMENT COMMENT '日志唯一标识',
  `user_id` INT NOT NULL COMMENT '操作用户ID',
  `action` VARCHAR(100) NOT NULL COMMENT '操作类型',
  `target_type` VARCHAR(50) DEFAULT NULL COMMENT '操作对象类型',
  `target_id` INT DEFAULT NULL COMMENT '操作对象ID',
  `detail` TEXT DEFAULT NULL COMMENT '操作详情',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`log_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_created_at` (`created_at`),
  FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

CREATE TABLE `statistics` (
  `stat_id` INT NOT NULL AUTO_INCREMENT COMMENT '统计记录唯一标识',
  `stat_month` VARCHAR(7) NOT NULL COMMENT '统计月份，格式 YYYY-MM',
  `region_type` VARCHAR(20) NOT NULL COMMENT '统计区域类型',
  `region_code` VARCHAR(50) NOT NULL COMMENT '区域编码',
  `region_name` VARCHAR(100) NOT NULL COMMENT '区域名称',
  `total_reports` INT DEFAULT 0 COMMENT '上报总数',
  `completed_reports` INT DEFAULT 0 COMMENT '已完成上报数',
  `avg_aqi` DECIMAL(8,2) DEFAULT 0 COMMENT '平均AQI值',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`stat_id`),
  UNIQUE KEY `uk_month_region` (`stat_month`, `region_type`, `region_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='统计数据表';

CREATE TABLE `user_points` (
  `user_id` INT NOT NULL COMMENT '用户ID',
  `total_points` INT DEFAULT 0 COMMENT '总积分',
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`user_id`),
  FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户积分表';
