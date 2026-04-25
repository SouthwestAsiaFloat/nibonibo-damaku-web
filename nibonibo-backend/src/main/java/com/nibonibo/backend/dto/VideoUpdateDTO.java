package com.nibonibo.backend.dto;

import lombok.Data;

@Data
public class VideoUpdateDTO {

    private String title;
    private String description;
    private String coverUrl;
    private String videoUrl;
    private Integer duration;
    private Long categoryId;
    private Integer status;
}
