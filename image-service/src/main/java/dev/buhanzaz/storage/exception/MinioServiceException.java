package dev.buhanzaz.storage.exception;

public class MinioServiceException extends RuntimeException {
    public MinioServiceException(String message, Exception e) {
        super(message, e);
    }
}
