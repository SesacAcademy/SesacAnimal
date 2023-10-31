package com.project.animal.global.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    // MiniO 설정
    @Value("${minio.endpoint.url}")
    private String minioUrl;

    @Value("${minio.accessKey}")
    private String accessKey;

    @Value("${minio.secretKey}")
    private String secretKey;

    @Bean
    public MinioClient createMinioClient() {
        return MinioClient.builder().endpoint(minioUrl)
                .credentials(accessKey, secretKey)
                .build();
    }
}
