package com.nibonibo.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nibonibo.backend.dto.UserLoginDTO;
import com.nibonibo.backend.dto.UserRegisterDTO;
import com.nibonibo.backend.entity.User;
import com.nibonibo.backend.vo.UserVO;

public interface UserService extends IService<User> {

    UserVO register(UserRegisterDTO dto);

    UserVO login(UserLoginDTO dto);

    UserVO getCurrentUser(Long userId);
}
