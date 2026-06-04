package ru.yandex.practicum.shopping_cart.exception;

import ru.yandex.practicum.api.exception.BaseApiException;

public class NotFoundCartException extends BaseApiException {
    public NotFoundCartException(String message) {
        super(message, "Корзина не найдена.");
    }
}
