package com.aishare.service.impl;

import com.aishare.common.BusinessException;
import com.aishare.dto.LoginDTO;
import com.aishare.dto.RegisterDTO;
import com.aishare.entity.User;
import com.aishare.mapper.UserMapper;
import com.aishare.service.AuthService;
import com.aishare.util.JwtUtil;
import com.aishare.vo.LoginVO;
import com.aishare.vo.UserVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public LoginVO register(RegisterDTO dto) {
        long count = userMapper.selectCount(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, dto.getEmail())
                .or()
                .eq(User::getUsername, dto.getUsername()));
        if (count > 0) throw new BusinessException("用户名或邮箱已存在");

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userMapper.insert(user);
        return buildLoginVO(user);
    }

    @Override
    public LoginVO login(LoginDTO dto) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, dto.getAccount())
                .or()
                .eq(User::getUsername, dto.getAccount()));
        if (user == null || !passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException("账号或密码错误");
        }
        return buildLoginVO(user);
    }

    private LoginVO buildLoginVO(User user) {
        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setUsername(user.getUsername());
        userVO.setEmail(user.getEmail());
        userVO.setAvatar(user.getAvatar());
        userVO.setBio(user.getBio());
        userVO.setCreatedAt(user.getCreatedAt());

        LoginVO vo = new LoginVO();
        vo.setToken(jwtUtil.generate(user.getId(), user.getUsername()));
        vo.setUser(userVO);
        return vo;
    }
}
