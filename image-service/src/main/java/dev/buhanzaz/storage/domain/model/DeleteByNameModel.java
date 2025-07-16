package dev.buhanzaz.storage.domain.model;

public record DeleteByNameModel(
        String bucketName,
        String path,
        String objectName
) {
}

