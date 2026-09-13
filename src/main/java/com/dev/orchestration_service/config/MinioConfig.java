package com.dev.orchestration_service.config;

import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig
{
    private final MinioProps minioProps;

    public MinioConfig(MinioProps minioProps) {this.minioProps = minioProps;}

    @Bean
    public MinioClient minioClient()
    {
        return MinioClient.builder()
                .endpoint(minioProps.getUrl())
                .credentials(minioProps.getAccessKey(), minioProps.getSecretKey())
                .build();
    }
}
