package com.nibonibo.backend.controller;

import com.nibonibo.backend.common.Result;
import com.nibonibo.backend.dto.DanmakuCreateDTO;
import com.nibonibo.backend.entity.Danmaku;
import com.nibonibo.backend.service.DanmakuService;
import com.nibonibo.backend.util.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "弹幕模块")
@RestController
@RequestMapping("/api/danmaku")
@RequiredArgsConstructor
public class DanmakuController {

    private final DanmakuService danmakuService;

    @Operation(summary = "获取视频弹幕")
    @GetMapping("/video/{videoId}")
    public Result<List<Danmaku>> listByVideo(@PathVariable Long videoId) {
        return Result.success(danmakuService.listByVideo(videoId));
    }

    @Operation(summary = "发送弹幕")
    @PostMapping
    public Result<Danmaku> create(@Valid @RequestBody DanmakuCreateDTO dto) {
        return Result.success(danmakuService.createDanmaku(UserContext.getUserId(), dto));
    }
}
