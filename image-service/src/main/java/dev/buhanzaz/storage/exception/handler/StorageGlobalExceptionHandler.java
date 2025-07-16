package dev.buhanzaz.storage.exception.handler;

import dev.buhanzaz.storage.domain.dto.response.ErrorResponse;
import dev.buhanzaz.storage.exception.MinioServiceException;
import dev.buhanzaz.storage.exception.MinioException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.io.UncheckedIOException;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@ControllerAdvice
public class StorageGlobalExceptionHandler {

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler({
            IllegalArgumentException.class,
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class,
            HttpMessageNotReadableException.class,
            MissingServletRequestPartException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequestException(Exception e) {
        log.error(e.getMessage(), e);

        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(),
                ExceptionUtils.getStackTrace(e),
                System.currentTimeMillis(),
                BAD_REQUEST);

        return ResponseEntity.status(BAD_REQUEST).body(errorResponse);
    }

    @ResponseStatus(PAYLOAD_TOO_LARGE)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(Exception e) {
        log.error(e.getMessage(), e);

        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(),
                ExceptionUtils.getStackTrace(e),
                System.currentTimeMillis(),
                PAYLOAD_TOO_LARGE);

        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler({
            MinioServiceException.class,
            UncheckedIOException.class,
            Exception.class
    })
    public ResponseEntity<ErrorResponse> handleStorageServiceException(Exception e) {
        log.error(e.getMessage(), e);

        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(),
                ExceptionUtils.getStackTrace(e),
                System.currentTimeMillis(),
                INTERNAL_SERVER_ERROR);

        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(errorResponse);
    }


    @ExceptionHandler(MinioException.class)
    public ResponseEntity<ErrorResponse> handleMinioBucketException(MinioException e) {
        log.error(e.getMessage(), e);

        HttpStatus httpStatus = e.getStatus();
        if (httpStatus == null) {
            httpStatus = INTERNAL_SERVER_ERROR;
        }

        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(),
                ExceptionUtils.getStackTrace(e),
                System.currentTimeMillis(),
                httpStatus
        );

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }
}
