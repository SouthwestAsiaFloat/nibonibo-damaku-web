package com.nibonibo.backend.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String NIBO_EXCHANGE = "nibonibo.exchange";
    public static final String VIDEO_PLAY_QUEUE = "nibonibo.video.play.queue";
    public static final String MESSAGE_NOTIFY_QUEUE = "nibonibo.message.notify.queue";
    public static final String VIDEO_TRANSCODE_QUEUE = "nibonibo.video.transcode.queue";
    public static final String VIDEO_PLAY_ROUTING_KEY = "video.play";
    public static final String MESSAGE_NOTIFY_ROUTING_KEY = "message.notify";
    public static final String VIDEO_TRANSCODE_ROUTING_KEY = "video.transcode";

    @Bean
    public DirectExchange niboniboExchange() {
        return new DirectExchange(NIBO_EXCHANGE, true, false);
    }

    @Bean
    public Queue videoPlayQueue() {
        return new Queue(VIDEO_PLAY_QUEUE, true);
    }

    @Bean
    public Queue messageNotifyQueue() {
        return new Queue(MESSAGE_NOTIFY_QUEUE, true);
    }

    @Bean
    public Queue videoTranscodeQueue() {
        return new Queue(VIDEO_TRANSCODE_QUEUE, true);
    }

    @Bean
    public Binding videoPlayBinding() {
        return BindingBuilder.bind(videoPlayQueue()).to(niboniboExchange()).with(VIDEO_PLAY_ROUTING_KEY);
    }

    @Bean
    public Binding messageNotifyBinding() {
        return BindingBuilder.bind(messageNotifyQueue()).to(niboniboExchange()).with(MESSAGE_NOTIFY_ROUTING_KEY);
    }

    @Bean
    public Binding videoTranscodeBinding() {
        return BindingBuilder.bind(videoTranscodeQueue()).to(niboniboExchange()).with(VIDEO_TRANSCODE_ROUTING_KEY);
    }
}
