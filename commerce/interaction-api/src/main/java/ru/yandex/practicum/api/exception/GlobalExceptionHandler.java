package ru.yandex.practicum.api.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseApiException.class)
    public ResponseEntity<ApiException> handleBaseApiException(BaseApiException ex) {
        ApiException errorBody = ApiException.builder()
                .cause(ex.getCause())
                .stackTrace(ex.getStackTrace())
                .httpStatus(ex.getHttpStatus().name())
                .userMessage(ex.getUserMessage())
                .message(ex.getMessage())
                .suppressed(ex.getSuppressed())
                .localizedMessage(ex.getLocalizedMessage())
                .build();

        return ResponseEntity.status(ex.getHttpStatus()).body(errorBody);
    }
}
