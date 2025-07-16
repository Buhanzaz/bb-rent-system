package dev.buhanzaz.storage.configuration;

import dev.buhanzaz.storage.property.MinioProperty;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MinIO Configuration for connecting to the MinIO server
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(MinioProperty.class)
@RequiredArgsConstructor
public class MinIOConfig {
    
    private final MinioProperty minioProperty;
    
    @Bean
    public MinioClient minioClient() {
        return MinioClient
                .builder()
                .endpoint(minioProperty.endpoint())
                .credentials(minioProperty.accessKey(), minioProperty.secretKey())
                .region(minioProperty.region())
                .build();
    }
}
