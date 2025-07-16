package dev.buhanzaz.storage.service.impl;

import dev.buhanzaz.storage.domain.model.*;
import dev.buhanzaz.storage.exception.MinioServiceException;
import dev.buhanzaz.storage.service.MinioService;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/* Добваить версионирование*/
@Slf4j
@Service
@RequiredArgsConstructor
class MinioServiceImpl implements MinioService {
    private final MinioClient minioClient;

    @Override
    public void upload(UploadModel model) {
        sendFile(model.getFullObjectName(), model.getContentType(), model.getFileInputStream(), model.bucketName());
    }

    @Override
    public void update(UpdateModel model) {
        sendFile(model.getFullObjectName(), model.getContentType(), model.getFileInputStream(), model.bucketName());
    }


    @Override
    public List<String> getTemporaryUrls(DownloadModel model) {
        String bucketName = model.bucketName();
        String path = model.path();

        List<Item> allObjectByPath = getAllObjectByPath(bucketName, path);

        return allObjectByPath.stream()
                .map(item -> getTemporaryFileUrl(model, item))
                .toList();
    }

    private String getTemporaryFileUrl(DownloadModel model, Item item) {
        String bucketName = model.bucketName();
        Integer expires = model.expires();
        TimeUnit timeUnit = model.timeUnit();
        Method method = model.method();

        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs
                            .builder()
                            .expiry(expires, timeUnit)
                            .bucket(bucketName)
                            .object(item.objectName())
                            .method(method)
                            .build()
            );
        } catch (Exception e) {
            throw new MinioServiceException("Failed to get presigned object url", e);
        }
    }


    @Override
    public void deleteByName(DeleteByNameModel model) {
        String bucketName = model.bucketName();
        String objectName = model.objectName();

        RemoveObjectArgs removeObjectArgs = RemoveObjectArgs
                .builder()
                .bucket(bucketName)
                .object(objectName)
                .build();

        try {
            minioClient.removeObject(removeObjectArgs);
        } catch (Exception e) {
            throw new MinioServiceException("Failed to delete object", e);
        }
    }

    @Override
    public void deleteAllWithPath(DeleteWithPathModel model) {
        String bucketName = model.bucketName();
        String path = model.path();

        List<Item> objects = getAllObjectByPath(bucketName, path);

        List<DeleteObject> deleteObjects = objects
                .stream()
                .map(item -> new DeleteObject(item.objectName()))
                .toList();

        RemoveObjectsArgs removeObjectsArgs = RemoveObjectsArgs
                .builder()
                .bucket(bucketName)
                .objects(deleteObjects)
                .build();

        minioClient.removeObjects(removeObjectsArgs);
    }

    private List<Item> getAllObjectByPath(String bucketName, String path) {

        List<Item> items = new ArrayList<>();

        Iterable<Result<Item>> results =
                minioClient.listObjects(ListObjectsArgs
                        .builder()
                        .bucket(bucketName)
                        .prefix(path)
                        .recursive(true)
                        .build()
                );

        if (results != null) {
            for (Result<Item> object : results) {
                Item item;
                try {
                    item = object.get();
                } catch (Exception e) {
                    throw new MinioServiceException("Failed to get object by path", e);
                }
                items.add(item);
            }
        }

        return items;
    }

    private void sendFile(String fullObjectName, String contentType, InputStream fileInputStream, String s) {
        try (InputStream is = fileInputStream) {
            minioClient.putObject(
                    PutObjectArgs
                            .builder()
                            .bucket(s)
                            .object(fullObjectName)
                            .contentType(contentType)
                            .stream(is, -1, ObjectWriteArgs.MIN_MULTIPART_SIZE)
                            .build()
            );
        } catch (Exception e) {
            throw new MinioServiceException("Failed to upload object", e);
        }
    }
}
