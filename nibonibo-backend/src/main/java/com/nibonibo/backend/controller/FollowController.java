package com.nibonibo.backend.controller;

import com.nibonibo.backend.common.Result;
import com.nibonibo.backend.service.FollowService;
import com.nibonibo.backend.util.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "关注模块")
@RestController
@RequestMapping("/api/follows")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @Operation(summary = "关注用户")
    @PostMapping("/users/{targetUserId}")
    public Result<Void> followUser(@PathVariable Long targetUserId) {
        followService.followUser(UserContext.getUserId(), targetUserId);
        return Result.success();
    }

    @Operation(summary = "取消关注用户")
    @DeleteMapping("/users/{targetUserId}")
    public Result<Void> unfollowUser(@PathVariable Long targetUserId) {
        followService.unfollowUser(UserContext.getUserId(), targetUserId);
        return Result.success();
    }
}
