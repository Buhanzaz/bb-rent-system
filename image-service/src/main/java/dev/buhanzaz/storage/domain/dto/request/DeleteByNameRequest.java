package dev.buhanzaz.storage.domain.dto.request;

public record DeleteByNameRequest(
        String bucketName,
        String path,
        String objectName
) {
}
