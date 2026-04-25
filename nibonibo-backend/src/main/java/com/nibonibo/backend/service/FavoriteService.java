package com.nibonibo.backend.service;

import com.nibonibo.backend.entity.Video;

import java.util.List;

public interface FavoriteService {

    List<Video> listFavoriteVideos(Long userId);

    void favoriteVideo(Long userId, Long videoId);

    void unfavoriteVideo(Long userId, Long videoId);
}
