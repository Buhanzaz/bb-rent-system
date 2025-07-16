package dev.buhanzaz.storage.domain.model;

import io.minio.http.Method;

import java.util.concurrent.TimeUnit;

public record DownloadModel(
        String bucketName,
        String path,
        Integer expires,
        TimeUnit timeUnit,
        Method method
) {

}
