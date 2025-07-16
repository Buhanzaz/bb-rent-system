package dev.buhanzaz.storage.domain.model;

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

public record UpdateModel(
        String bucketName,
        String path,
        String fileName,
        MultipartFile file
) {

    public InputStream getFileInputStream() {
        try {
            return this.file.getInputStream();
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to get input stream from file", e);
        }
    }

    public String getFullObjectName() {
        return "%s/%s/%s".formatted(
                this.bucketName,
                this.path,
                this.fileName
        );
    }

    public String getContentType() {
        String stringContentType = this.file.getContentType();

        return stringContentType == null ? MediaType.APPLICATION_OCTET_STREAM.toString() : stringContentType;
    }
}