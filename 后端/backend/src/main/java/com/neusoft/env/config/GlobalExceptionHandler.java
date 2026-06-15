package com.neusoft.env.config;

import com.neusoft.env.dto.response.ApiResponse;
import com.neusoft.env.exception.AuthException;
import com.neusoft.env.exception.LoginFailedException;
import com.neusoft.env.exception.PhoneExistsException;
import com.neusoft.env.exception.UserNotFoundException;
import com.neusoft.env.exception.UsernameExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理器
 * 统一处理各类异常，返回友好的错误信息
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        ApiResponse<Map<String, String>> response = new ApiResponse<>();
        response.setCode(400);
        response.setMessage("参数校验失败");
        response.setData(errors);
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * 处理用户名已存在异常
     */
    @ExceptionHandler(UsernameExistsException.class)
    public ResponseEntity<ApiResponse<Object>> handleUsernameExistsException(UsernameExistsException ex) {
        ApiResponse<Object> response = new ApiResponse<>();
        response.setCode(ex.getCode());
        response.setMessage(ex.getMessage());
        response.setData(null);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    /**
     * 处理手机号已存在异常
     */
    @ExceptionHandler(PhoneExistsException.class)
    public ResponseEntity<ApiResponse<Object>> handlePhoneExistsException(PhoneExistsException ex) {
        ApiResponse<Object> response = new ApiResponse<>();
        response.setCode(ex.getCode());
        response.setMessage(ex.getMessage());
        response.setData(null);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    /**
     * 处理登录失败异常
     */
    @ExceptionHandler(LoginFailedException.class)
    public ResponseEntity<ApiResponse<Object>> handleLoginFailedException(LoginFailedException ex) {
        ApiResponse<Object> response = new ApiResponse<>();
        response.setCode(ex.getCode());
        response.setMessage(ex.getMessage());
        response.setData(null);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    /**
     * 处理用户不存在异常
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserNotFoundException(UserNotFoundException ex) {
        ApiResponse<Object> response = new ApiResponse<>();
        response.setCode(ex.getCode());
        response.setMessage(ex.getMessage());
        response.setData(null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * 处理通用认证异常
     */
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ApiResponse<Object>> handleAuthException(AuthException ex) {
        ApiResponse<Object> response = new ApiResponse<>();
        response.setCode(ex.getCode());
        response.setMessage(ex.getMessage());
        response.setData(null);
        return ResponseEntity.status(ex.getCode()).body(response);
    }

    /**
     * 处理其他运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> handleRuntimeException(RuntimeException ex) {
        ApiResponse<Object> response = new ApiResponse<>();
        response.setCode(500);
        response.setMessage(ex.getMessage());
        response.setData(null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * 处理未知异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception ex) {
        ApiResponse<Object> response = new ApiResponse<>();
        response.setCode(500);
        response.setMessage("系统内部错误");
        response.setData(null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
