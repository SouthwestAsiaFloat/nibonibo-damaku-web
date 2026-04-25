package com.nibonibo.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VideoCreateDTO {

    @NotBlank(message = "标题不能为空")
    private String title;
    private String description;
    private String coverUrl;
    private String videoUrl;
    private Integer duration;
    private Long categoryId;
}
