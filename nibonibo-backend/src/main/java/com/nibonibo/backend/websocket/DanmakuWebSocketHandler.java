package com.nibonibo.backend.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class DanmakuWebSocketHandler extends TextWebSocketHandler {

    private static final String VIDEO_ID_ATTRIBUTE = "videoId";
    private final ConcurrentHashMap<String, Set<WebSocketSession>> rooms = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String videoId = parseVideoId(session.getUri());
        session.getAttributes().put(VIDEO_ID_ATTRIBUTE, videoId);
        rooms.computeIfAbsent(videoId, key -> ConcurrentHashMap.newKeySet()).add(session);
        log.info("Danmaku websocket connected. videoId={}, sessionId={}", videoId, session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String videoId = String.valueOf(session.getAttributes().get(VIDEO_ID_ATTRIBUTE));
        for (WebSocketSession target : rooms.getOrDefault(videoId, Set.of())) {
            if (target.isOpen()) {
                target.sendMessage(message);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String videoId = String.valueOf(session.getAttributes().get(VIDEO_ID_ATTRIBUTE));
        Set<WebSocketSession> sessions = rooms.get(videoId);
        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                rooms.remove(videoId);
            }
        }
        log.info("Danmaku websocket closed. videoId={}, sessionId={}", videoId, session.getId());
    }

    private String parseVideoId(URI uri) {
        if (uri == null) {
            return "unknown";
        }
        String path = uri.getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }
}
