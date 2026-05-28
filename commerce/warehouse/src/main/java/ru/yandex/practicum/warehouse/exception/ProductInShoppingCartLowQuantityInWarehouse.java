package ru.yandex.practicum.warehouse.exception;

import org.springframework.http.HttpStatus;
import ru.yandex.practicum.api.exception.BaseApiException;

public class ProductInShoppingCartLowQuantityInWarehouse extends BaseApiException {
    public ProductInShoppingCartLowQuantityInWarehouse(String message) {
        super(message, HttpStatus.NOT_FOUND, "Выбранного товара на складе осталось меньше, чем добавил в корзину");
    }
}
