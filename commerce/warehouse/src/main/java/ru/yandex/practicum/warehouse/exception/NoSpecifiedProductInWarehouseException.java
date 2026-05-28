package ru.yandex.practicum.warehouse.exception;

import org.springframework.http.HttpStatus;
import ru.yandex.practicum.api.exception.BaseApiException;

public class NoSpecifiedProductInWarehouseException extends BaseApiException {
    public NoSpecifiedProductInWarehouseException(String message) {
        super(message, HttpStatus.NOT_FOUND, "Запрашиваемый товар или продукт отсутствует на указанном складе");
    }
}
