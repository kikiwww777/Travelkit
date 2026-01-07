package com.example.check.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 文件上传控制器
 */
@RestController
@RequestMapping("/api/upload")
@CrossOrigin(origins = "*")
public class UploadController {

    // 上传文件保存的目录（相对于项目根目录）
    private static final String UPLOAD_DIR = "uploads/images/";
    
    // 访问URL前缀
    private static final String URL_PREFIX = "/uploads/images/";

    /**
     * 上传图片文件
     * @param file 上传的文件
     * @return 上传结果，包含图片URL
     */
    @PostMapping("/image")
    public Map<String, Object> uploadImage(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 检查文件是否为空
            if (file == null || file.isEmpty()) {
                result.put("success", false);
                result.put("message", "文件不能为空");
                return result;
            }
            
            // 检查文件类型
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) {
                result.put("success", false);
                result.put("message", "文件名不能为空");
                return result;
            }
            
            String extension = getFileExtension(originalFilename);
            if (!isImageFile(extension)) {
                result.put("success", false);
                result.put("message", "只支持图片文件（jpg, jpeg, png, gif, webp）");
                return result;
            }
            
            // 检查文件大小（限制为10MB）
            long maxSize = 10 * 1024 * 1024; // 10MB
            if (file.getSize() > maxSize) {
                result.put("success", false);
                result.put("message", "文件大小不能超过10MB");
                return result;
            }
            
            // 生成唯一文件名
            String fileName = UUID.randomUUID().toString() + "." + extension;
            
            // 创建上传目录（如果不存在）
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            // 保存文件
            Path filePath = Paths.get(UPLOAD_DIR + fileName);
            Files.write(filePath, file.getBytes());
            
            // 返回文件URL（相对路径，前端可以拼接BASE_URL）
            String fileUrl = URL_PREFIX + fileName;
            
            result.put("success", true);
            result.put("data", fileUrl);
            result.put("message", "上传成功");
            
        } catch (IOException e) {
            result.put("success", false);
            result.put("message", "文件上传失败：" + e.getMessage());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "上传失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == filename.length() - 1) {
            return "";
        }
        return filename.substring(lastDotIndex + 1).toLowerCase();
    }
    
    /**
     * 检查是否为图片文件
     */
    private boolean isImageFile(String extension) {
        if (extension == null || extension.isEmpty()) {
            return false;
        }
        String[] imageExtensions = {"jpg", "jpeg", "png", "gif", "webp"};
        for (String ext : imageExtensions) {
            if (ext.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }
}

