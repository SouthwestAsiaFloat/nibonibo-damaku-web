package com.nibonibo.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nibonibo.backend.common.BusinessException;
import com.nibonibo.backend.common.ErrorCode;
import com.nibonibo.backend.entity.UserFollow;
import com.nibonibo.backend.mapper.UserFollowMapper;
import com.nibonibo.backend.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final UserFollowMapper userFollowMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void followUser(Long userId, Long targetUserId) {
        if (userId.equals(targetUserId)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "不能关注自己");
        }
        Long count = userFollowMapper.selectCount(new LambdaQueryWrapper<UserFollow>()
                .eq(UserFollow::getUserId, userId)
                .eq(UserFollow::getTargetUserId, targetUserId));
        if (count > 0) {
            throw new BusinessException(ErrorCode.CONFLICT, "已经关注过了");
        }

        LocalDateTime now = LocalDateTime.now();
        UserFollow follow = new UserFollow();
        follow.setUserId(userId);
        follow.setTargetUserId(targetUserId);
        follow.setCreatedAt(now);
        follow.setUpdatedAt(now);
        follow.setDeleted(0);
        userFollowMapper.insert(follow);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unfollowUser(Long userId, Long targetUserId) {
        UserFollow follow = userFollowMapper.selectOne(new LambdaQueryWrapper<UserFollow>()
                .eq(UserFollow::getUserId, userId)
                .eq(UserFollow::getTargetUserId, targetUserId)
                .last("LIMIT 1"));
        if (follow != null) {
            userFollowMapper.deleteById(follow.getId());
        }
    }
}
