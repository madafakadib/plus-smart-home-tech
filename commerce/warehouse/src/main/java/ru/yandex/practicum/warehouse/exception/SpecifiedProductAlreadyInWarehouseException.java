package ru.yandex.practicum.warehouse.exception;

import org.springframework.http.HttpStatus;
import ru.yandex.practicum.api.exception.BaseApiException;

public class SpecifiedProductAlreadyInWarehouseException extends BaseApiException {
    public SpecifiedProductAlreadyInWarehouseException(String message) {
        super(message, HttpStatus.NOT_FOUND, "Вы пытаетесь повторно добавить или зарегистрировать товар на складе, где он уже существует");
    }
}
