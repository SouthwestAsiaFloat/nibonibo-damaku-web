package com.nibonibo.backend.controller;

import com.nibonibo.backend.common.Result;
import com.nibonibo.backend.service.InteractionService;
import com.nibonibo.backend.util.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "互动模块")
@RestController
@RequestMapping("/api/interactions")
@RequiredArgsConstructor
public class InteractionController {

    private final InteractionService interactionService;

    @Operation(summary = "点赞视频")
    @PostMapping("/videos/{videoId}/like")
    public Result<Void> likeVideo(@PathVariable Long videoId) {
        interactionService.likeVideo(UserContext.getUserId(), videoId);
        return Result.success();
    }

    @Operation(summary = "取消点赞视频")
    @DeleteMapping("/videos/{videoId}/like")
    public Result<Void> unlikeVideo(@PathVariable Long videoId) {
        interactionService.unlikeVideo(UserContext.getUserId(), videoId);
        return Result.success();
    }
}
