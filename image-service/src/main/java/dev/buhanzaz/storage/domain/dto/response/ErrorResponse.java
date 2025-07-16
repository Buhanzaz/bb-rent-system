package dev.buhanzaz.storage.domain.dto.response;

import org.springframework.http.HttpStatus;

public record ErrorResponse(
        String message,
        String details,
        long timestamp,
        HttpStatus statusCode
) {
}
