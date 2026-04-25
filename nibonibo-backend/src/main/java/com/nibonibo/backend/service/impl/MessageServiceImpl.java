package com.nibonibo.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nibonibo.backend.common.BusinessException;
import com.nibonibo.backend.common.ErrorCode;
import com.nibonibo.backend.entity.Message;
import com.nibonibo.backend.mapper.MessageMapper;
import com.nibonibo.backend.service.MessageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Override
    public List<Message> listMine(Long userId) {
        return lambdaQuery()
                .eq(Message::getUserId, userId)
                .orderByDesc(Message::getCreatedAt)
                .list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markRead(Long userId, Long id) {
        Message message = getById(id);
        if (message == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "消息不存在");
        }
        if (!message.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "只能读取自己的消息");
        }
        message.setReadStatus(1);
        message.setUpdatedAt(LocalDateTime.now());
        updateById(message);
    }
}
