package ru.yandex.practicum.shopping_store.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.api.exception.ApiException;

import java.util.Collections;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundRuntimeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiException handleProductNotFound(ProductNotFoundRuntimeException ex) {

        return ApiException.builder()
                .userMessage("Ошибка, товар по идентификатору не найден")
                .message(ex.getMessage())
                .suppressed(Collections.emptyList())
                .localizedMessage(ex.getLocalizedMessage())
                .build();
    }
}
