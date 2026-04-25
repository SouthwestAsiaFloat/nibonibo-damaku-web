package com.nibonibo.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nibonibo.backend.dto.CommentCreateDTO;
import com.nibonibo.backend.entity.Comment;

import java.util.List;

public interface CommentService extends IService<Comment> {

    List<Comment> listByVideo(Long videoId);

    Comment createComment(Long userId, CommentCreateDTO dto);

    void deleteComment(Long userId, Long id);
}
