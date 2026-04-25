package com.nibonibo.backend.service.impl;

import com.nibonibo.backend.common.BusinessException;
import com.nibonibo.backend.common.ErrorCode;
import com.nibonibo.backend.config.MinioConfig;
import com.nibonibo.backend.service.UploadService;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadServiceImpl implements UploadService {

    private final MinioClient minioClient;
    private final MinioConfig minioConfig;

    @Override
    public String uploadImage(MultipartFile file) {
        return upload(file, "images");
    }

    @Override
    public String uploadVideo(MultipartFile file) {
        return upload(file, "videos");
    }

    private String upload(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "文件不能为空");
        }

        try {
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioConfig.getBucket()).build());
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioConfig.getBucket()).build());
            }

            String objectName = folder + "/" + UUID.randomUUID() + getExtension(file.getOriginalFilename());
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minioConfig.getBucket())
                    .object(objectName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
            return trimTrailingSlash(minioConfig.getPublicUrl()) + "/" + minioConfig.getBucket() + "/" + objectName;
        } catch (Exception exception) {
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "文件上传失败：" + exception.getMessage());
        }
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.'));
    }

    private String trimTrailingSlash(String value) {
        if (value.endsWith("/")) {
            return value.substring(0, value.length() - 1);
        }
        return value;
    }
}
