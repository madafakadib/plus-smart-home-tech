package ru.yandex.practicum.api.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseApiException.class)
    public ApiException handleBaseApiException(BaseApiException ex) {

        return ApiException.builder()
                .userMessage(ex.getUserMessage())
                .message(ex.getMessage())
                .localizedMessage(ex.getLocalizedMessage())
                .suppressed(Collections.emptyList())
                .cause(null)
                .stackTrace(Collections.emptyList())
                .build();
    }
}
