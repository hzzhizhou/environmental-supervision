package com.neusoft.env.service.auth;

import com.neusoft.env.dto.request.LoginRequest;
import com.neusoft.env.dto.request.RegisterRequest;
import com.neusoft.env.dto.response.LoginResponse;
import com.neusoft.env.dto.response.UserDTO;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    UserDTO register(RegisterRequest request);

    void logout(Integer userId);
}