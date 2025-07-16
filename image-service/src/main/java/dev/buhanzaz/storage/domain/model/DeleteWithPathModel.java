package dev.buhanzaz.storage.domain.model;

public record DeleteWithPathModel(
        String bucketName,
        String path
) {
}
