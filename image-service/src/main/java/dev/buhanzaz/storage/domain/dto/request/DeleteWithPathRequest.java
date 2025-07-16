package dev.buhanzaz.storage.domain.dto.request;

public record DeleteWithPathRequest(
        String bucketName,
        String path
) {
}
