package com.neusoft.env.service.auth.impl;

import com.neusoft.env.dto.request.LoginRequest;
import com.neusoft.env.dto.request.RegisterRequest;
import com.neusoft.env.dto.response.LoginResponse;
import com.neusoft.env.dto.response.UserDTO;
import com.neusoft.env.entity.Grid;
import com.neusoft.env.entity.Role;
import com.neusoft.env.entity.User;
import com.neusoft.env.config.JwtUtil;
import com.neusoft.env.exception.LoginFailedException;
import com.neusoft.env.exception.PhoneExistsException;
import com.neusoft.env.exception.UsernameExistsException;
import com.neusoft.env.mapper.GridMapper;
import com.neusoft.env.mapper.RoleMapper;
import com.neusoft.env.mapper.UserMapper;
import com.neusoft.env.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private GridMapper gridMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userMapper.findByUsername(request.getUsername());
        if (user == null) {
            throw new LoginFailedException("用户名不存在");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new LoginFailedException("密码错误");
        }

        if (request.getRoleId() != null && !request.getRoleId().equals(user.getRoleId())) {
            throw new LoginFailedException("该用户不属于所选角色");
        }

        if (user.getStatus() != 1) {
            throw new RuntimeException("用户已被禁用");
        }

        user.setLastLoginAt(LocalDateTime.now());
        userMapper.updateById(user);

        String token = jwtUtil.generateToken(user.getUserId(), user.getUsername(), user.getRoleId());
        LocalDateTime expireTime = LocalDateTime.now().plusSeconds(jwtUtil.getExpiration() / 1000);

        LoginResponse response = new LoginResponse();
        response.setUserId(user.getUserId());
        response.setUsername(user.getUsername());
        response.setRealName(user.getRealName());
        response.setPhone(user.getPhone());
        response.setRoleId(user.getRoleId());
        response.setGridId(user.getGridId());
        response.setToken(token);
        response.setExpireTime(expireTime);

        Role role = roleMapper.selectById(user.getRoleId());
        if (role != null) {
            response.setRoleName(role.getRoleName());
        }
        Grid grid = gridMapper.selectById(user.getGridId());
        if (grid != null) {
            response.setGridName(grid.getGridName());
        }

        return response;
    }

    @Override
    @Transactional
    public UserDTO register(RegisterRequest request) {
        if (!"123456".equals(request.getCode())) {
            throw new RuntimeException("验证码错误");
        }

        if (userMapper.existsByUsername(request.getUsername())) {
            throw new UsernameExistsException(request.getUsername());
        }

        if (userMapper.existsByPhone(request.getPhone())) {
            throw new PhoneExistsException(request.getPhone());
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRealName(request.getRealName());
        user.setPhone(request.getPhone());
        user.setRoleId(3);
        user.setGridId(null);
        user.setStatus(1);
        user.setCreatedAt(LocalDateTime.now());

        userMapper.insert(user);
        return convertToUserDTO(user);
    }

    @Override
    @Transactional
    public void logout(Integer userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setLastLoginAt(LocalDateTime.now());
        userMapper.updateById(user);
    }

    private UserDTO convertToUserDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setRealName(user.getRealName());
        dto.setPhone(user.getPhone());
        dto.setRoleId(user.getRoleId());
        dto.setGridId(user.getGridId());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setStatus(user.getStatus());
        dto.setStatusDesc(user.getStatus() == 1 ? "正常" : "禁用");
        dto.setLastLoginAt(user.getLastLoginAt());
        dto.setCreatedAt(user.getCreatedAt());

        Role role = roleMapper.selectById(user.getRoleId());
        if (role != null) {
            dto.setRoleName(role.getRoleName());
        }
        Grid grid = gridMapper.selectById(user.getGridId());
        if (grid != null) {
            dto.setGridName(grid.getGridName());
        }

        return dto;
    }
}