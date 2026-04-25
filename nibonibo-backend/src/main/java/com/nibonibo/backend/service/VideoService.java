package com.nibonibo.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nibonibo.backend.common.PageResult;
import com.nibonibo.backend.dto.VideoCreateDTO;
import com.nibonibo.backend.dto.VideoUpdateDTO;
import com.nibonibo.backend.entity.Video;

import java.util.List;

public interface VideoService extends IService<Video> {

    Video createVideo(Long userId, VideoCreateDTO dto);

    Video getVideoDetail(Long id);

    PageResult<Video> pageVideos(long pageNum, long pageSize, String keyword);

    List<Video> hotVideos(int limit);

    List<Video> listMine(Long userId);

    Video updateVideo(Long userId, Long id, VideoUpdateDTO dto);

    void deleteVideo(Long userId, Long id);
}
