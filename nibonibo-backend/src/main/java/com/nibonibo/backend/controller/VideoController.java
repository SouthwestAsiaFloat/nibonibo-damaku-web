package com.nibonibo.backend.controller;

import com.nibonibo.backend.common.PageResult;
import com.nibonibo.backend.common.Result;
import com.nibonibo.backend.dto.VideoCreateDTO;
import com.nibonibo.backend.dto.VideoUpdateDTO;
import com.nibonibo.backend.entity.Video;
import com.nibonibo.backend.service.VideoService;
import com.nibonibo.backend.util.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "视频模块")
@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @Operation(summary = "发布视频")
    @PostMapping
    public Result<Video> create(@Valid @RequestBody VideoCreateDTO dto) {
        return Result.success(videoService.createVideo(UserContext.getUserId(), dto));
    }

    @Operation(summary = "视频详情")
    @GetMapping("/{id}")
    public Result<Video> detail(@PathVariable Long id) {
        return Result.success(videoService.getVideoDetail(id));
    }

    @Operation(summary = "分页查询视频")
    @GetMapping("/page")
    public Result<PageResult<Video>> page(@RequestParam(defaultValue = "1") long pageNum,
                                          @RequestParam(defaultValue = "10") long pageSize,
                                          @RequestParam(required = false) String keyword) {
        return Result.success(videoService.pageVideos(pageNum, pageSize, keyword));
    }

    @Operation(summary = "热门视频")
    @GetMapping("/hot")
    public Result<List<Video>> hot(@RequestParam(defaultValue = "10") int limit) {
        return Result.success(videoService.hotVideos(limit));
    }

    @Operation(summary = "我的投稿")
    @GetMapping("/mine")
    public Result<List<Video>> mine() {
        return Result.success(videoService.listMine(UserContext.getUserId()));
    }

    @Operation(summary = "更新视频")
    @PutMapping("/{id}")
    public Result<Video> update(@PathVariable Long id, @RequestBody VideoUpdateDTO dto) {
        return Result.success(videoService.updateVideo(UserContext.getUserId(), id, dto));
    }

    @Operation(summary = "删除视频")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        videoService.deleteVideo(UserContext.getUserId(), id);
        return Result.success();
    }
}
