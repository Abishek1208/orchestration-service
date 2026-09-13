package com.dev.orchestration_service.minio;

import com.dev.orchestration_service.config.MinioProps;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class MinioStorageService
{

    private final MinioProps minioProperties;
    private final MinioClient minioClient;

    public MinioStorageService(MinioProps minioProperties, MinioClient minioClient)
    {
        this.minioProperties = minioProperties;
        this.minioClient = minioClient;
    }

    public void upload(String fileName, InputStream inputStream, String contentType)
            throws Exception
    {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .object(fileName)
                            .stream(inputStream, -1, 10485760)
                            .contentType(contentType)
                            .build()
            );
        }
        catch (Exception e) {
            throw new Exception("Failed to upload to MinIO", e);
        }
    }
}

