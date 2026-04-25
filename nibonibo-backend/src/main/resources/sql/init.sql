CREATE DATABASE IF NOT EXISTS `nibonibo`
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_0900_ai_ci;

USE `nibonibo`;

CREATE TABLE IF NOT EXISTS `user` (
  `id` BIGINT NOT NULL COMMENT '主键 ID',
  `username` VARCHAR(64) NOT NULL COMMENT '用户名',
  `password` VARCHAR(255) NOT NULL COMMENT 'BCrypt 密码',
  `nickname` VARCHAR(64) NOT NULL COMMENT '昵称',
  `avatar` VARCHAR(512) DEFAULT NULL COMMENT '头像 URL',
  `bio` VARCHAR(512) DEFAULT NULL COMMENT '个人简介',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1 正常，0 禁用',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0 未删除，1 已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_username` (`username`),
  KEY `idx_user_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';

CREATE TABLE IF NOT EXISTS `category` (
  `id` BIGINT NOT NULL COMMENT '主键 ID',
  `name` VARCHAR(64) NOT NULL COMMENT '分区名称',
  `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT '父分区 ID',
  `sort` INT NOT NULL DEFAULT 0 COMMENT '排序',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0 未删除，1 已删除',
  PRIMARY KEY (`id`),
  KEY `idx_category_parent_id` (`parent_id`),
  KEY `idx_category_sort` (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='视频分区表';

CREATE TABLE IF NOT EXISTS `video` (
  `id` BIGINT NOT NULL COMMENT '主键 ID',
  `user_id` BIGINT NOT NULL COMMENT 'UP 主 ID',
  `title` VARCHAR(160) NOT NULL COMMENT '视频标题',
  `description` TEXT DEFAULT NULL COMMENT '视频简介',
  `cover_url` VARCHAR(512) DEFAULT NULL COMMENT '封面 URL',
  `video_url` VARCHAR(512) DEFAULT NULL COMMENT '视频 URL',
  `duration` INT DEFAULT 0 COMMENT '视频时长，单位秒',
  `category_id` BIGINT DEFAULT NULL COMMENT '分区 ID',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0 草稿，1 已发布，2 审核中，3 下架',
  `view_count` BIGINT NOT NULL DEFAULT 0 COMMENT '播放量',
  `like_count` BIGINT NOT NULL DEFAULT 0 COMMENT '点赞数',
  `favorite_count` BIGINT NOT NULL DEFAULT 0 COMMENT '收藏数',
  `comment_count` BIGINT NOT NULL DEFAULT 0 COMMENT '评论数',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0 未删除，1 已删除',
  PRIMARY KEY (`id`),
  KEY `idx_video_user_id` (`user_id`),
  KEY `idx_video_category_id` (`category_id`),
  KEY `idx_video_status_created_at` (`status`, `created_at`),
  KEY `idx_video_hot` (`view_count`, `like_count`),
  FULLTEXT KEY `ft_video_title_description` (`title`, `description`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='视频表';

CREATE TABLE IF NOT EXISTS `danmaku` (
  `id` BIGINT NOT NULL COMMENT '主键 ID',
  `video_id` BIGINT NOT NULL COMMENT '视频 ID',
  `user_id` BIGINT NOT NULL COMMENT '用户 ID',
  `content` VARCHAR(255) NOT NULL COMMENT '弹幕内容',
  `time_offset` INT NOT NULL DEFAULT 0 COMMENT '视频内时间偏移，单位毫秒',
  `color` VARCHAR(32) NOT NULL DEFAULT '#ffffff' COMMENT '弹幕颜色',
  `font_size` INT NOT NULL DEFAULT 25 COMMENT '字体大小',
  `mode` TINYINT NOT NULL DEFAULT 1 COMMENT '弹幕模式：1 滚动，2 顶部，3 底部',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0 未删除，1 已删除',
  PRIMARY KEY (`id`),
  KEY `idx_danmaku_video_time` (`video_id`, `time_offset`),
  KEY `idx_danmaku_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='弹幕表';

CREATE TABLE IF NOT EXISTS `comment` (
  `id` BIGINT NOT NULL COMMENT '主键 ID',
  `video_id` BIGINT NOT NULL COMMENT '视频 ID',
  `user_id` BIGINT NOT NULL COMMENT '评论用户 ID',
  `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT '父评论 ID，0 表示一级评论',
  `root_id` BIGINT NOT NULL DEFAULT 0 COMMENT '根评论 ID，0 表示一级评论',
  `content` TEXT NOT NULL COMMENT '评论内容',
  `like_count` BIGINT NOT NULL DEFAULT 0 COMMENT '评论点赞数',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1 正常，0 隐藏',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0 未删除，1 已删除',
  PRIMARY KEY (`id`),
  KEY `idx_comment_video_created_at` (`video_id`, `created_at`),
  KEY `idx_comment_parent_id` (`parent_id`),
  KEY `idx_comment_root_id` (`root_id`),
  KEY `idx_comment_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='评论表';

CREATE TABLE IF NOT EXISTS `video_like` (
  `id` BIGINT NOT NULL COMMENT '主键 ID',
  `video_id` BIGINT NOT NULL COMMENT '视频 ID',
  `user_id` BIGINT NOT NULL COMMENT '用户 ID',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '预留删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_video_like_video_user` (`video_id`, `user_id`),
  KEY `idx_video_like_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='视频点赞表';

CREATE TABLE IF NOT EXISTS `video_favorite` (
  `id` BIGINT NOT NULL COMMENT '主键 ID',
  `video_id` BIGINT NOT NULL COMMENT '视频 ID',
  `user_id` BIGINT NOT NULL COMMENT '用户 ID',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '预留删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_video_favorite_video_user` (`video_id`, `user_id`),
  KEY `idx_video_favorite_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='视频收藏表';

CREATE TABLE IF NOT EXISTS `user_follow` (
  `id` BIGINT NOT NULL COMMENT '主键 ID',
  `user_id` BIGINT NOT NULL COMMENT '关注者 ID',
  `target_user_id` BIGINT NOT NULL COMMENT '被关注者 ID',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '预留删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_follow_user_target` (`user_id`, `target_user_id`),
  KEY `idx_user_follow_target_user_id` (`target_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户关注表';

CREATE TABLE IF NOT EXISTS `message` (
  `id` BIGINT NOT NULL COMMENT '主键 ID',
  `user_id` BIGINT NOT NULL COMMENT '接收用户 ID',
  `from_user_id` BIGINT DEFAULT NULL COMMENT '触发用户 ID',
  `type` VARCHAR(32) NOT NULL COMMENT '消息类型：LIKE、COMMENT、FOLLOW、SYSTEM',
  `biz_id` BIGINT DEFAULT NULL COMMENT '关联业务 ID',
  `content` VARCHAR(512) NOT NULL COMMENT '消息内容',
  `read_status` TINYINT NOT NULL DEFAULT 0 COMMENT '阅读状态：0 未读，1 已读',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0 未删除，1 已删除',
  PRIMARY KEY (`id`),
  KEY `idx_message_user_read_created` (`user_id`, `read_status`, `created_at`),
  KEY `idx_message_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='消息表';

INSERT INTO `category` (`id`, `name`, `parent_id`, `sort`, `created_at`, `updated_at`, `deleted`) VALUES
(1001, '动画', 0, 1, NOW(), NOW(), 0),
(1002, '游戏', 0, 2, NOW(), NOW(), 0),
(1003, '科技', 0, 3, NOW(), NOW(), 0),
(1004, '生活', 0, 4, NOW(), NOW(), 0),
(1005, '美食', 0, 5, NOW(), NOW(), 0)
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`), `sort` = VALUES(`sort`);
