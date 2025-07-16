package dev.buhanzaz.storage.domain.dto.response;

import io.minio.http.Method;

import java.util.List;
import java.util.concurrent.TimeUnit;

public record DownloadResponse(
        String bucketName,
        String path,
        Integer expires,
        TimeUnit timeUnit,
        Method method,
        List<String> temporaryURLs
) {
}
