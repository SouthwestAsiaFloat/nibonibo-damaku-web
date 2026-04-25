package com.nibonibo.backend.service;

public interface InteractionService {

    void likeVideo(Long userId, Long videoId);

    void unlikeVideo(Long userId, Long videoId);
}
