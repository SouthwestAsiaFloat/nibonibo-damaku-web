package com.nibonibo.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nibonibo.backend.common.BusinessException;
import com.nibonibo.backend.common.ErrorCode;
import com.nibonibo.backend.dto.CommentCreateDTO;
import com.nibonibo.backend.entity.Comment;
import com.nibonibo.backend.entity.Video;
import com.nibonibo.backend.mapper.CommentMapper;
import com.nibonibo.backend.mapper.VideoMapper;
import com.nibonibo.backend.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    private final VideoMapper videoMapper;

    @Override
    public List<Comment> listByVideo(Long videoId) {
        return lambdaQuery()
                .eq(Comment::getVideoId, videoId)
                .orderByDesc(Comment::getCreatedAt)
                .list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Comment createComment(Long userId, CommentCreateDTO dto) {
        Long rootId = dto.getRootId();
        if (dto.getParentId() != null && rootId == null) {
            Comment parent = getById(dto.getParentId());
            if (parent == null) {
                throw new BusinessException(ErrorCode.NOT_FOUND, "父评论不存在");
            }
            rootId = parent.getRootId() == null || parent.getRootId() == 0 ? parent.getId() : parent.getRootId();
        }

        LocalDateTime now = LocalDateTime.now();
        Comment comment = new Comment();
        comment.setVideoId(dto.getVideoId());
        comment.setUserId(userId);
        comment.setParentId(dto.getParentId() == null ? 0L : dto.getParentId());
        comment.setRootId(rootId == null ? 0L : rootId);
        comment.setContent(dto.getContent());
        comment.setLikeCount(0L);
        comment.setStatus(1);
        comment.setCreatedAt(now);
        comment.setUpdatedAt(now);
        comment.setDeleted(0);
        save(comment);

        videoMapper.update(null, new LambdaUpdateWrapper<Video>()
                .eq(Video::getId, dto.getVideoId())
                .setSql("comment_count = comment_count + 1"));
        return comment;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteComment(Long userId, Long id) {
        Comment comment = getById(id);
        if (comment == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "评论不存在");
        }
        if (!comment.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "只能删除自己的评论");
        }
        removeById(id);
        videoMapper.update(null, new LambdaUpdateWrapper<Video>()
                .eq(Video::getId, comment.getVideoId())
                .setSql("comment_count = GREATEST(comment_count - 1, 0)"));
    }
}
