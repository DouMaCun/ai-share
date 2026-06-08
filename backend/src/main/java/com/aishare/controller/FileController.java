package com.aishare.controller;

import com.aishare.common.BusinessException;
import com.aishare.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api/upload")
public class FileController {

    private static final Set<String> ALLOWED_TYPES = Set.of(
            "image/jpeg", "image/png", "image/gif", "image/webp"
    );
    private static final long MAX_SIZE = 5 * 1024 * 1024; // 5MB

    private final Path uploadDir;

    public FileController(@Value("${app.upload.dir:uploads}") String uploadDir) {
        this.uploadDir = Paths.get(uploadDir, "covers").toAbsolutePath();
        try {
            Files.createDirectories(this.uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("无法创建上传目录", e);
        }
    }

    @PostMapping("/image")
    public Result<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) throw new BusinessException("文件为空");

        if (file.getSize() > MAX_SIZE) throw new BusinessException("文件大小不能超过5MB");

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType)) {
            throw new BusinessException("仅支持 JPG / PNG / GIF / WebP 格式");
        }

        String ext = getExtension(Objects.requireNonNull(file.getOriginalFilename()));
        String filename = UUID.randomUUID() + ext;

        try {
            Path targetPath = uploadDir.resolve(filename);
            file.transferTo(targetPath.toFile());
            log.info("文件上传成功: {}", targetPath);
        } catch (IOException e) {
            log.error("文件保存失败", e);
            throw new BusinessException("文件保存失败");
        }

        Map<String, String> data = new HashMap<>();
        data.put("url", "/uploads/covers/" + filename);
        return Result.ok(data);
    }

    private String getExtension(String filename) {
        int dot = filename.lastIndexOf('.');
        if (dot < 0) return "";
        return filename.substring(dot).toLowerCase();
    }
}