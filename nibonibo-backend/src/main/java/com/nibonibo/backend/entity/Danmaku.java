package com.nibonibo.backend.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("danmaku")
public class Danmaku {

    @TableId
    private Long id;
    private Long videoId;
    private Long userId;
    private String content;
    private Integer timeOffset;
    private String color;
    private Integer fontSize;
    private Integer mode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @TableLogic
    private Integer deleted;
}
