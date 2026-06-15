package com.neusoft.env.service.file.impl;

import com.neusoft.env.service.file.FileUploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Value("${file.upload.path:uploads/}")
    private String uploadPath;

    @Override
    public String uploadFile(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("上传文件不能为空");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = System.currentTimeMillis() + extension;

        String folderPath = uploadPath + folder + "/";
        File uploadDir = new File(folderPath);
        if (!uploadDir.exists()) {
            boolean created = uploadDir.mkdirs();
        }

        try {
            File dest = new File(folderPath + newFilename);
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("文件上传失败: " + e.getMessage());
        }

        return "/api/file/" + folder + "/" + newFilename;
    }
}