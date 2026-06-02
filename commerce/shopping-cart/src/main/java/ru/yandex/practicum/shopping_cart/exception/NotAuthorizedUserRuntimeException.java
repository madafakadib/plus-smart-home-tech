package ru.yandex.practicum.shopping_cart.exception;

import ru.yandex.practicum.api.exception.BaseApiException;

public class NotAuthorizedUserRuntimeException extends BaseApiException {
    public NotAuthorizedUserRuntimeException(String message) {
        super(message, "Пользователь не авторизован.");
    }
}
