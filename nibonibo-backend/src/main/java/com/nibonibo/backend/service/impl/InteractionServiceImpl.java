package com.nibonibo.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.nibonibo.backend.common.BusinessException;
import com.nibonibo.backend.common.ErrorCode;
import com.nibonibo.backend.entity.Video;
import com.nibonibo.backend.entity.VideoLike;
import com.nibonibo.backend.mapper.VideoLikeMapper;
import com.nibonibo.backend.mapper.VideoMapper;
import com.nibonibo.backend.service.InteractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InteractionServiceImpl implements InteractionService {

    private final VideoLikeMapper videoLikeMapper;
    private final VideoMapper videoMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void likeVideo(Long userId, Long videoId) {
        Long count = videoLikeMapper.selectCount(new LambdaQueryWrapper<VideoLike>()
                .eq(VideoLike::getUserId, userId)
                .eq(VideoLike::getVideoId, videoId));
        if (count > 0) {
            throw new BusinessException(ErrorCode.CONFLICT, "已经点赞过了");
        }

        LocalDateTime now = LocalDateTime.now();
        VideoLike like = new VideoLike();
        like.setUserId(userId);
        like.setVideoId(videoId);
        like.setCreatedAt(now);
        like.setUpdatedAt(now);
        like.setDeleted(0);
        videoLikeMapper.insert(like);
        videoMapper.update(null, new LambdaUpdateWrapper<Video>()
                .eq(Video::getId, videoId)
                .setSql("like_count = like_count + 1"));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unlikeVideo(Long userId, Long videoId) {
        VideoLike like = videoLikeMapper.selectOne(new LambdaQueryWrapper<VideoLike>()
                .eq(VideoLike::getUserId, userId)
                .eq(VideoLike::getVideoId, videoId)
                .last("LIMIT 1"));
        if (like == null) {
            return;
        }
        videoLikeMapper.deleteById(like.getId());
        videoMapper.update(null, new LambdaUpdateWrapper<Video>()
                .eq(Video::getId, videoId)
                .setSql("like_count = GREATEST(like_count - 1, 0)"));
    }
}
