package com.nibonibo.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.nibonibo.backend.common.BusinessException;
import com.nibonibo.backend.common.ErrorCode;
import com.nibonibo.backend.entity.Video;
import com.nibonibo.backend.entity.VideoFavorite;
import com.nibonibo.backend.mapper.VideoFavoriteMapper;
import com.nibonibo.backend.mapper.VideoMapper;
import com.nibonibo.backend.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final VideoFavoriteMapper videoFavoriteMapper;
    private final VideoMapper videoMapper;

    @Override
    public List<Video> listFavoriteVideos(Long userId) {
        List<VideoFavorite> favorites = videoFavoriteMapper.selectList(new LambdaQueryWrapper<VideoFavorite>()
                .eq(VideoFavorite::getUserId, userId)
                .orderByDesc(VideoFavorite::getCreatedAt));
        List<Long> videoIds = favorites.stream().map(VideoFavorite::getVideoId).toList();
        if (videoIds.isEmpty()) {
            return Collections.emptyList();
        }
        return videoMapper.selectBatchIds(videoIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void favoriteVideo(Long userId, Long videoId) {
        Long count = videoFavoriteMapper.selectCount(new LambdaQueryWrapper<VideoFavorite>()
                .eq(VideoFavorite::getUserId, userId)
                .eq(VideoFavorite::getVideoId, videoId));
        if (count > 0) {
            throw new BusinessException(ErrorCode.CONFLICT, "已经收藏过了");
        }

        LocalDateTime now = LocalDateTime.now();
        VideoFavorite favorite = new VideoFavorite();
        favorite.setUserId(userId);
        favorite.setVideoId(videoId);
        favorite.setCreatedAt(now);
        favorite.setUpdatedAt(now);
        favorite.setDeleted(0);
        videoFavoriteMapper.insert(favorite);
        videoMapper.update(null, new LambdaUpdateWrapper<Video>()
                .eq(Video::getId, videoId)
                .setSql("favorite_count = favorite_count + 1"));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unfavoriteVideo(Long userId, Long videoId) {
        VideoFavorite favorite = videoFavoriteMapper.selectOne(new LambdaQueryWrapper<VideoFavorite>()
                .eq(VideoFavorite::getUserId, userId)
                .eq(VideoFavorite::getVideoId, videoId)
                .last("LIMIT 1"));
        if (favorite == null) {
            return;
        }
        videoFavoriteMapper.deleteById(favorite.getId());
        videoMapper.update(null, new LambdaUpdateWrapper<Video>()
                .eq(Video::getId, videoId)
                .setSql("favorite_count = GREATEST(favorite_count - 1, 0)"));
    }
}
