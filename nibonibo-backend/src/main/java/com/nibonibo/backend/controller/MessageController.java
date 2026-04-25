package com.nibonibo.backend.controller;

import com.nibonibo.backend.common.Result;
import com.nibonibo.backend.entity.Message;
import com.nibonibo.backend.service.MessageService;
import com.nibonibo.backend.util.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "消息模块")
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @Operation(summary = "我的消息")
    @GetMapping
    public Result<List<Message>> listMine() {
        return Result.success(messageService.listMine(UserContext.getUserId()));
    }

    @Operation(summary = "标记已读")
    @PostMapping("/{id}/read")
    public Result<Void> markRead(@PathVariable Long id) {
        messageService.markRead(UserContext.getUserId(), id);
        return Result.success();
    }
}
