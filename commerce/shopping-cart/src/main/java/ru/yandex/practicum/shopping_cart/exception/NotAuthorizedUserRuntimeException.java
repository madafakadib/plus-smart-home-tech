package ru.yandex.practicum.shopping_cart.exception;

import org.springframework.http.HttpStatus;
import ru.yandex.practicum.api.exception.BaseApiException;

public class NotAuthorizedUserRuntimeException extends BaseApiException {
    public NotAuthorizedUserRuntimeException(String message) {
        super(message, HttpStatus.UNAUTHORIZED, "Пользователь не авторизован.");
    }
}
