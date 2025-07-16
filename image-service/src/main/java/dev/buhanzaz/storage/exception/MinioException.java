package dev.buhanzaz.storage.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MinioException extends RuntimeException {
    private final HttpStatus status;
    public MinioException(String message, Exception e, HttpStatus status) {
        super(message, e);
        this.status = status;
    }

    public MinioException(Exception e) {
        super(e);
        this.status = null;
    }
}
