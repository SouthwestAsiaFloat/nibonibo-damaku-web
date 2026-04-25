package com.nibonibo.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentCreateDTO {

    @NotNull(message = "视频 ID 不能为空")
    private Long videoId;
    private Long parentId;
    private Long rootId;

    @NotBlank(message = "评论内容不能为空")
    private String content;
}
