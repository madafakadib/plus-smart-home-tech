package ru.yandex.practicum.shopping_store.exception;

import org.springframework.http.HttpStatus;
import ru.yandex.practicum.api.exception.BaseApiException;

public class ProductNotFoundRuntimeException extends BaseApiException {
    public ProductNotFoundRuntimeException(String message) {
        super(message, HttpStatus.NOT_FOUND, "Запрашиваемый товар не найден.");
    }
}
