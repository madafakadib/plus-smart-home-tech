package ru.yandex.practicum.shopping_cart.exception;

import org.springframework.http.HttpStatus;
import ru.yandex.practicum.api.exception.BaseApiException;

public class NotFoundCartException extends BaseApiException {
    public NotFoundCartException(String message) {
        super(message, HttpStatus.NOT_FOUND, "Корзина не найдена.");
    }
}
