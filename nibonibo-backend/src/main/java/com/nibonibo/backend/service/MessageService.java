package com.nibonibo.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nibonibo.backend.entity.Message;

import java.util.List;

public interface MessageService extends IService<Message> {

    List<Message> listMine(Long userId);

    void markRead(Long userId, Long id);
}
