package com.aishare.controller;

import com.aishare.common.Result;
import com.aishare.entity.User;
import com.aishare.mapper.UserMapper;
import com.aishare.util.UserContext;
import com.aishare.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserMapper userMapper;

    @GetMapping("/me")
    public Result<UserVO> me() {
        Long userId = UserContext.get();
        User user = userMapper.selectById(userId);
        if (user == null) return Result.fail("用户不存在");
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setEmail(user.getEmail());
        vo.setAvatar(user.getAvatar());
        vo.setBio(user.getBio());
        vo.setCreatedAt(user.getCreatedAt());
        return Result.ok(vo);
    }

    @PutMapping("/me")
    public Result<Void> updateProfile(@RequestBody UserVO dto) {
        Long userId = UserContext.get();
        User user = new User();
        user.setId(userId);
        user.setAvatar(dto.getAvatar());
        user.setBio(dto.getBio());
        userMapper.updateById(user);
        return Result.ok();
    }
}
