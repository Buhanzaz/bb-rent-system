package dev.buhanzaz.storage.domain.dto.request;

public record UploadRequest(
        String bucketName,
        String path
) {
}
