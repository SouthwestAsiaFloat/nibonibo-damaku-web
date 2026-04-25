package com.nibonibo.backend.controller;

import com.nibonibo.backend.common.Result;
import com.nibonibo.backend.entity.Video;
import com.nibonibo.backend.service.FavoriteService;
import com.nibonibo.backend.util.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "收藏模块")
@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @Operation(summary = "我的收藏视频")
    @GetMapping("/videos")
    public Result<List<Video>> listFavoriteVideos() {
        return Result.success(favoriteService.listFavoriteVideos(UserContext.getUserId()));
    }

    @Operation(summary = "收藏视频")
    @PostMapping("/videos/{videoId}")
    public Result<Void> favoriteVideo(@PathVariable Long videoId) {
        favoriteService.favoriteVideo(UserContext.getUserId(), videoId);
        return Result.success();
    }

    @Operation(summary = "取消收藏视频")
    @DeleteMapping("/videos/{videoId}")
    public Result<Void> unfavoriteVideo(@PathVariable Long videoId) {
        favoriteService.unfavoriteVideo(UserContext.getUserId(), videoId);
        return Result.success();
    }
}
