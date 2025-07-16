package dev.buhanzaz.storage.domain.model;

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

public record UploadModel(
        String bucketName,
        String path,
        MultipartFile file,
        Long iteration) {

    public InputStream getFileInputStream() {
        try {
            return this.file.getInputStream();
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to get input stream from file", e);
        }
    }

    public String getFullObjectName() {
        return "%s/%s/%d-%s.%s".formatted(
                this.bucketName,
                this.path,
                this.iteration,
                this.hashCode(),
                this.getExtension()
        );
    }

    public String getContentType() {
        String stringContentType = this.file.getContentType();

        return stringContentType == null ? MediaType.APPLICATION_OCTET_STREAM.toString() : stringContentType;
    }

    private String getExtension() {
        String originalFilename = this.file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new IllegalArgumentException("File must have an extension");
        }
        return originalFilename.substring(originalFilename.lastIndexOf('.') + 1);
    }
}
