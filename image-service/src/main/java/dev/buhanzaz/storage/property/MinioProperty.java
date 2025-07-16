package dev.buhanzaz.storage.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "minio.credentials")
public record MinioProperty(
        String endpoint,
        String accessKey,
        String secretKey,
        String region
) {
}
