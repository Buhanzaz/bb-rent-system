package dev.buhanzaz.storage.domain.dto.request;

public record UpdateRequest(
        String bucketName,
        String path,
        String fileName
) {
}
