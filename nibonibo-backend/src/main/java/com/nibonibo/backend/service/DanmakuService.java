package com.nibonibo.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nibonibo.backend.dto.DanmakuCreateDTO;
import com.nibonibo.backend.entity.Danmaku;

import java.util.List;

public interface DanmakuService extends IService<Danmaku> {

    List<Danmaku> listByVideo(Long videoId);

    Danmaku createDanmaku(Long userId, DanmakuCreateDTO dto);
}
