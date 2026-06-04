package ru.yandex.practicum.warehouse.exception;

import ru.yandex.practicum.api.exception.BaseApiException;

public class SpecifiedProductAlreadyInWarehouseException extends BaseApiException {
    public SpecifiedProductAlreadyInWarehouseException(String message) {
        super(message, "Вы пытаетесь повторно добавить или зарегистрировать товар на складе, где он уже существует");
    }
}
