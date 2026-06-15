package com.neusoft.env.controller.profile;

import com.neusoft.env.dto.request.ChangePasswordRequest;
import com.neusoft.env.dto.request.UpdateProfileRequest;
import com.neusoft.env.dto.response.ApiResponse;
import com.neusoft.env.dto.response.UserDTO;
import com.neusoft.env.service.profile.ProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Value("${file.upload.path:uploads/}")
    private String uploadPath;

    @GetMapping("/info/{userId}")
    public ResponseEntity<ApiResponse<UserDTO>> getProfile(@PathVariable Integer userId) {
        UserDTO user = profileService.getProfile(userId);
        return ResponseEntity.ok(ApiResponse.success("获取成功", user));
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse<UserDTO>> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        UserDTO user = profileService.updateProfile(request);
        return ResponseEntity.ok(ApiResponse.success("个人信息修改成功", user));
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        profileService.changePassword(request);
        return ResponseEntity.ok(ApiResponse.success("密码修改成功", null));
    }

    @PostMapping("/upload-avatar")
    public ResponseEntity<ApiResponse<String>> uploadAvatar(
            @RequestParam Integer userId,
            @RequestParam("file") MultipartFile file) {
        String avatarUrl = profileService.uploadAvatar(userId, file);
        return ResponseEntity.ok(ApiResponse.success("头像上传成功", avatarUrl));
    }

    @GetMapping("/avatar/{filename}")
    public ResponseEntity<Resource> getAvatar(@PathVariable String filename) {
        File file = new File(uploadPath + "avatar/" + filename);
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(file);
        String contentType = getContentType(filename);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                .body(resource);
    }

    private String getContentType(String filename) {
        String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        return switch (extension) {
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            case "bmp" -> "image/bmp";
            case "webp" -> "image/webp";
            default -> "application/octet-stream";
        };
    }
}