package com.neusoft.env.service.profile.impl;

import com.neusoft.env.dto.request.ChangePasswordRequest;
import com.neusoft.env.dto.request.UpdateProfileRequest;
import com.neusoft.env.dto.response.UserDTO;
import com.neusoft.env.entity.Grid;
import com.neusoft.env.entity.Role;
import com.neusoft.env.entity.User;
import com.neusoft.env.exception.PhoneExistsException;
import com.neusoft.env.mapper.GridMapper;
import com.neusoft.env.mapper.RoleMapper;
import com.neusoft.env.mapper.UserMapper;
import com.neusoft.env.service.file.FileUploadService;
import com.neusoft.env.service.profile.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private GridMapper gridMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private FileUploadService fileUploadService;

    @Override
    public UserDTO getProfile(Integer userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        return convertToUserDTO(user);
    }

    @Override
    @Transactional
    public UserDTO updateProfile(UpdateProfileRequest request) {
        User user = userMapper.selectById(request.getUserId());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        if (request.getRealName() != null) {
            user.setRealName(request.getRealName());
        }

        if (request.getPhone() != null && !request.getPhone().equals(user.getPhone())) {
            if (userMapper.existsByPhone(request.getPhone())) {
                throw new PhoneExistsException(request.getPhone());
            }
            user.setPhone(request.getPhone());
        }

        userMapper.updateById(user);
        return convertToUserDTO(user);
    }

    @Override
    @Transactional
    public UserDTO updateProfile(Integer userId, String realName, String phone, String password) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        if (realName != null && !realName.isEmpty()) {
            user.setRealName(realName);
        }

        if (phone != null && !phone.isEmpty() && !phone.equals(user.getPhone())) {
            if (userMapper.existsByPhone(phone)) {
                throw new PhoneExistsException(phone);
            }
            user.setPhone(phone);
        }

        if (password != null && !password.isEmpty()) {
            user.setPassword(passwordEncoder.encode(password));
        }

        userMapper.updateById(user);
        return convertToUserDTO(user);
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        User user = userMapper.selectById(request.getUserId());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("旧密码错误");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userMapper.updateById(user);
    }

    @Override
    @Transactional
    public String uploadAvatar(Integer userId, MultipartFile file) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        String avatarUrl = fileUploadService.uploadFile(file, "avatar");
        user.setAvatarUrl(avatarUrl);
        userMapper.updateById(user);

        return avatarUrl;
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