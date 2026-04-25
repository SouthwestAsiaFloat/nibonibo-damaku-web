package com.nibonibo.backend.util;

import com.nibonibo.backend.config.RabbitMqConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMqProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendVideoPlay(Object payload) {
        safeSend(RabbitMqConfig.VIDEO_PLAY_ROUTING_KEY, payload);
    }

    public void sendMessageNotify(Object payload) {
        safeSend(RabbitMqConfig.MESSAGE_NOTIFY_ROUTING_KEY, payload);
    }

    public void sendVideoTranscode(Object payload) {
        safeSend(RabbitMqConfig.VIDEO_TRANSCODE_ROUTING_KEY, payload);
    }

    private void safeSend(String routingKey, Object payload) {
        try {
            rabbitTemplate.convertAndSend(RabbitMqConfig.NIBO_EXCHANGE, routingKey, payload);
        } catch (Exception exception) {
            log.warn("RabbitMQ send skipped. routingKey={}, reason={}", routingKey, exception.getMessage());
        }
    }
}
