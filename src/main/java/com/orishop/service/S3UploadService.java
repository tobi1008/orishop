package com.orishop.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class S3UploadService {

    @Value("${app.upload.dir:/app/uploads/}")
    private String uploadDir;

    @PostConstruct
    public void init() {
        try {
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                boolean created = directory.mkdirs();
                if (created) {
                    System.out.println("Đã tạo thư mục lưu trữ ảnh: " + uploadDir);
                }
            }
        } catch (Exception e) {
            System.err.println("Không thể khởi tạo thư mục lưu trữ ảnh: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            return null;
        }
        try {
            // Tạo tên file độc nhất để không bị đè ảnh cũ (VD: 1234-5678_ao-thun.jpg)
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            
            // Đường dẫn đích để lưu file
            Path targetPath = Paths.get(uploadDir).resolve(fileName);
            
            // Sao chép file vào thư mục đích (đè lên nếu trùng tên)
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            // Trả về đường dẫn tương đối để Client truy cập qua static resource mapping
            return "/uploads/" + fileName;
        } catch (IOException e) {
            System.err.println("Lỗi ghi file local: " + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            System.err.println("Lỗi upload file local: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
