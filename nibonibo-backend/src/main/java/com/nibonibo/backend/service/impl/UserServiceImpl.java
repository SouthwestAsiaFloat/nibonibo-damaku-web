package com.nibonibo.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nibonibo.backend.common.BusinessException;
import com.nibonibo.backend.common.ErrorCode;
import com.nibonibo.backend.dto.UserLoginDTO;
import com.nibonibo.backend.dto.UserRegisterDTO;
import com.nibonibo.backend.entity.User;
import com.nibonibo.backend.mapper.UserMapper;
import com.nibonibo.backend.security.JwtUtil;
import com.nibonibo.backend.service.UserService;
import com.nibonibo.backend.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVO register(UserRegisterDTO dto) {
        Long count = lambdaQuery().eq(User::getUsername, dto.getUsername()).count();
        if (count > 0) {
            throw new BusinessException(ErrorCode.CONFLICT, "用户名已存在");
        }

        LocalDateTime now = LocalDateTime.now();
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setNickname(StringUtils.hasText(dto.getNickname()) ? dto.getNickname() : dto.getUsername());
        user.setAvatar("https://api.dicebear.com/9.x/thumbs/svg?seed=" + dto.getUsername());
        user.setBio("这个人很神秘，还没有写简介。");
        user.setStatus(1);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        user.setDeleted(0);
        save(user);

        UserVO vo = toVO(user);
        vo.setToken(jwtUtil.generateToken(user.getId(), user.getUsername()));
        return vo;
    }

    @Override
    public UserVO login(UserLoginDTO dto) {
        User user = lambdaQuery().eq(User::getUsername, dto.getUsername()).one();
        if (user == null || !passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "用户名或密码错误");
        }
        if (Integer.valueOf(0).equals(user.getStatus())) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "账号已被禁用");
        }

        UserVO vo = toVO(user);
        vo.setToken(jwtUtil.generateToken(user.getId(), user.getUsername()));
        return vo;
    }

    @Override
    public UserVO getCurrentUser(Long userId) {
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "用户不存在");
        }
        return toVO(user);
    }

    private UserVO toVO(User user) {
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        vo.setAvatar(user.getAvatar());
        vo.setBio(user.getBio());
        return vo;
    }
}
