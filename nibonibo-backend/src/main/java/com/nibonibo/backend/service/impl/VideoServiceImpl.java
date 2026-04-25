package com.nibonibo.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nibonibo.backend.common.BusinessException;
import com.nibonibo.backend.common.ErrorCode;
import com.nibonibo.backend.common.PageResult;
import com.nibonibo.backend.dto.VideoCreateDTO;
import com.nibonibo.backend.dto.VideoUpdateDTO;
import com.nibonibo.backend.entity.Video;
import com.nibonibo.backend.mapper.VideoMapper;
import com.nibonibo.backend.service.VideoService;
import com.nibonibo.backend.util.RabbitMqProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {

    private final VideoMapper videoMapper;
    private final RabbitMqProducer rabbitMqProducer;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Video createVideo(Long userId, VideoCreateDTO dto) {
        LocalDateTime now = LocalDateTime.now();
        Video video = new Video();
        video.setUserId(userId);
        video.setTitle(dto.getTitle());
        video.setDescription(dto.getDescription());
        video.setCoverUrl(dto.getCoverUrl());
        video.setVideoUrl(dto.getVideoUrl());
        video.setDuration(dto.getDuration());
        video.setCategoryId(dto.getCategoryId());
        video.setStatus(1);
        video.setViewCount(0L);
        video.setLikeCount(0L);
        video.setFavoriteCount(0L);
        video.setCommentCount(0L);
        video.setCreatedAt(now);
        video.setUpdatedAt(now);
        video.setDeleted(0);
        save(video);
        rabbitMqProducer.sendVideoTranscode(Map.of("videoId", video.getId(), "videoUrl", video.getVideoUrl()));
        return video;
    }

    @Override
    public Video getVideoDetail(Long id) {
        Video video = getById(id);
        if (video == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "视频不存在");
        }
        videoMapper.update(null, new LambdaUpdateWrapper<Video>()
                .eq(Video::getId, id)
                .setSql("view_count = view_count + 1"));
        rabbitMqProducer.sendVideoPlay(Map.of("videoId", id));
        video.setViewCount(video.getViewCount() + 1);
        return video;
    }

    @Override
    public PageResult<Video> pageVideos(long pageNum, long pageSize, String keyword) {
        Page<Video> page = lambdaQuery()
                .like(StringUtils.hasText(keyword), Video::getTitle, keyword)
                .orderByDesc(Video::getCreatedAt)
                .page(Page.of(pageNum, pageSize));
        return PageResult.of(page);
    }

    @Override
    public List<Video> hotVideos(int limit) {
        return lambdaQuery()
                .orderByDesc(Video::getViewCount)
                .last("LIMIT " + Math.max(1, Math.min(limit, 50)))
                .list();
    }

    @Override
    public List<Video> listMine(Long userId) {
        return lambdaQuery()
                .eq(Video::getUserId, userId)
                .orderByDesc(Video::getCreatedAt)
                .list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Video updateVideo(Long userId, Long id, VideoUpdateDTO dto) {
        Video video = getById(id);
        if (video == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "视频不存在");
        }
        if (!video.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "只能修改自己的视频");
        }

        if (StringUtils.hasText(dto.getTitle())) {
            video.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            video.setDescription(dto.getDescription());
        }
        if (dto.getCoverUrl() != null) {
            video.setCoverUrl(dto.getCoverUrl());
        }
        if (dto.getVideoUrl() != null) {
            video.setVideoUrl(dto.getVideoUrl());
        }
        if (dto.getDuration() != null) {
            video.setDuration(dto.getDuration());
        }
        if (dto.getCategoryId() != null) {
            video.setCategoryId(dto.getCategoryId());
        }
        if (dto.getStatus() != null) {
            video.setStatus(dto.getStatus());
        }
        video.setUpdatedAt(LocalDateTime.now());
        updateById(video);
        return video;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteVideo(Long userId, Long id) {
        Video video = getById(id);
        if (video == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "视频不存在");
        }
        if (!video.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "只能删除自己的视频");
        }
        removeById(id);
    }
}
