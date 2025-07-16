package dev.buhanzaz.storage.domain.dto.request;

import io.minio.http.Method;

import java.util.concurrent.TimeUnit;


public record DownloadRequest(
        String bucketName,
        String path,
        Integer expires,
        TimeUnit timeUnit,
        Method method
) {
}
