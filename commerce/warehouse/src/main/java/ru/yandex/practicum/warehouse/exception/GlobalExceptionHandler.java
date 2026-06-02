package ru.yandex.practicum.warehouse.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.api.exception.ApiException;

import java.util.Collections;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSpecifiedProductInWarehouseException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiException handleProductNotFound(NoSpecifiedProductInWarehouseException ex) {

        return ApiException.builder()
                .userMessage("Ошибка, запрашиваемый товар или продукт отсутствует на указанном складе")
                .message(ex.getMessage())
                .suppressed(Collections.emptyList())
                .localizedMessage(ex.getLocalizedMessage())
                .build();
    }

    @ExceptionHandler(ProductInShoppingCartLowQuantityInWarehouse.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiException handleLowQuantity(ProductInShoppingCartLowQuantityInWarehouse ex) {

        return ApiException.builder()
                .userMessage("Выбранного товара на складе осталось меньше, чем добавлено в корзину")
                .message(ex.getMessage())
                .suppressed(Collections.emptyList())
                .localizedMessage(ex.getLocalizedMessage())
                .build();
    }

    @ExceptionHandler(SpecifiedProductAlreadyInWarehouseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiException handleProductAlreadyExists(SpecifiedProductAlreadyInWarehouseException ex) {

        return ApiException.builder()
                .userMessage("Вы пытаетесь повторно добавить или зарегистрировать товар на складе, где он уже существует")
                .message(ex.getMessage())
                .suppressed(Collections.emptyList())
                .localizedMessage(ex.getLocalizedMessage())
                .build();
    }
}
