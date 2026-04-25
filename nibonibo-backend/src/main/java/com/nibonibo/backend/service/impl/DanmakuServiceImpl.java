package com.nibonibo.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nibonibo.backend.dto.DanmakuCreateDTO;
import com.nibonibo.backend.entity.Danmaku;
import com.nibonibo.backend.mapper.DanmakuMapper;
import com.nibonibo.backend.service.DanmakuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DanmakuServiceImpl extends ServiceImpl<DanmakuMapper, Danmaku> implements DanmakuService {

    @Override
    public List<Danmaku> listByVideo(Long videoId) {
        return lambdaQuery()
                .eq(Danmaku::getVideoId, videoId)
                .orderByAsc(Danmaku::getTimeOffset)
                .list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Danmaku createDanmaku(Long userId, DanmakuCreateDTO dto) {
        LocalDateTime now = LocalDateTime.now();
        Danmaku danmaku = new Danmaku();
        danmaku.setVideoId(dto.getVideoId());
        danmaku.setUserId(userId);
        danmaku.setContent(dto.getContent());
        danmaku.setTimeOffset(dto.getTimeOffset() == null ? 0 : dto.getTimeOffset());
        danmaku.setColor(StringUtils.hasText(dto.getColor()) ? dto.getColor() : "#ffffff");
        danmaku.setFontSize(dto.getFontSize() == null ? 25 : dto.getFontSize());
        danmaku.setMode(dto.getMode() == null ? 1 : dto.getMode());
        danmaku.setCreatedAt(now);
        danmaku.setUpdatedAt(now);
        danmaku.setDeleted(0);
        save(danmaku);
        return danmaku;
    }
}
