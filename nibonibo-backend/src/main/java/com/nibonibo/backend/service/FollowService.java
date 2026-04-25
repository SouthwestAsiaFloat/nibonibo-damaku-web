package com.nibonibo.backend.service;

public interface FollowService {

    void followUser(Long userId, Long targetUserId);

    void unfollowUser(Long userId, Long targetUserId);
}
