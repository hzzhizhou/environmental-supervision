package com.neusoft.env.controller.auth;

import com.neusoft.env.dto.request.LoginRequest;
import com.neusoft.env.dto.request.RegisterRequest;
import com.neusoft.env.dto.response.ApiResponse;
import com.neusoft.env.dto.response.LoginResponse;
import com.neusoft.env.dto.response.UserDTO;
import com.neusoft.env.service.auth.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("登录成功", response));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDTO>> register(@Valid @RequestBody RegisterRequest request) {
        UserDTO user = authService.register(request);
        return ResponseEntity.ok(ApiResponse.success("注册成功", user));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestParam Integer userId) {
        authService.logout(userId);
        return ResponseEntity.ok(ApiResponse.success("退出成功", null));
    }
}