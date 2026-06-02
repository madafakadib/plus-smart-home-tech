package ru.yandex.practicum.shopping_store.exception;

import ru.yandex.practicum.api.exception.BaseApiException;

public class ProductNotFoundRuntimeException extends BaseApiException {
    public ProductNotFoundRuntimeException(String message) {
        super(message, "Запрашиваемый товар не найден.");
    }
}
