package com.neusoft.env.service.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {

    String uploadFile(MultipartFile file, String folder);
}