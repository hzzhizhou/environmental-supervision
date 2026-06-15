package com.neusoft.env.service.profile;

import com.neusoft.env.dto.request.ChangePasswordRequest;
import com.neusoft.env.dto.request.UpdateProfileRequest;
import com.neusoft.env.dto.response.UserDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileService {

    UserDTO getProfile(Integer userId);

    UserDTO updateProfile(UpdateProfileRequest request);

    UserDTO updateProfile(Integer userId, String realName, String phone, String password);

    void changePassword(ChangePasswordRequest request);

    String uploadAvatar(Integer userId, MultipartFile file);
}