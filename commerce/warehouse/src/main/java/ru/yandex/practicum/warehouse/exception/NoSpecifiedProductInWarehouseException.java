package ru.yandex.practicum.warehouse.exception;

import ru.yandex.practicum.api.exception.BaseApiException;

public class NoSpecifiedProductInWarehouseException extends BaseApiException {
    public NoSpecifiedProductInWarehouseException(String message) {
        super(message, "Запрашиваемый товар или продукт отсутствует на указанном складе");
    }
}
