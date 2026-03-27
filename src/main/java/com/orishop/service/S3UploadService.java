package com.orishop.service; // Sửa lại package cho đúng cấu trúc

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct; // Đổi thành jakarta vì dùng Spring Boot 3
import java.io.IOException;
import java.util.UUID;

@Service
public class S3UploadService {

    // Lấy tên Bucket và Region từ biến môi trường (đã khai báo trong file orishop-eks.yaml)
    @Value("${AWS_S3_BUCKET_NAME}")
    private String bucketName;

    @Value("${AWS_REGION}")
    private String region;

    private AmazonS3 s3Client;

    @PostConstruct
    public void init() {
        // Khởi tạo S3 Client. 
        // Lõi AWS SDK sẽ tự động lấy quyền từ IAM Role của EKS (DefaultAWSCredentialsProviderChain)
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .build();
    }

    public String uploadFile(MultipartFile file) {
        try {
            // Tạo tên file độc nhất để không bị đè ảnh cũ (VD: 1234-5678_ao-thun.jpg)
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            // Đẩy ảnh lên kho S3
            s3Client.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(), metadata));

            // Trả về đường link Public của ảnh để Controller lưu vào Database (RDS)
            return s3Client.getUrl(bucketName, fileName).toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
