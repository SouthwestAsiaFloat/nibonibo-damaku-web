package com.nibonibo.backend.controller;

import com.nibonibo.backend.common.Result;
import com.nibonibo.backend.dto.CommentCreateDTO;
import com.nibonibo.backend.entity.Comment;
import com.nibonibo.backend.service.CommentService;
import com.nibonibo.backend.util.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "评论模块")
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "获取视频评论")
    @GetMapping("/video/{videoId}")
    public Result<List<Comment>> listByVideo(@PathVariable Long videoId) {
        return Result.success(commentService.listByVideo(videoId));
    }

    @Operation(summary = "发表评论")
    @PostMapping
    public Result<Comment> create(@Valid @RequestBody CommentCreateDTO dto) {
        return Result.success(commentService.createComment(UserContext.getUserId(), dto));
    }

    @Operation(summary = "删除评论")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        commentService.deleteComment(UserContext.getUserId(), id);
        return Result.success();
    }
}
