package com.nibonibo.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DanmakuCreateDTO {

    @NotNull(message = "视频 ID 不能为空")
    private Long videoId;

    @NotBlank(message = "弹幕内容不能为空")
    private String content;

    private Integer timeOffset;
    private String color;
    private Integer fontSize;
    private Integer mode;
}
