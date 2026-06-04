package ru.yandex.practicum.warehouse.exception;

import ru.yandex.practicum.api.exception.BaseApiException;

public class ProductInShoppingCartLowQuantityInWarehouse extends BaseApiException {
    public ProductInShoppingCartLowQuantityInWarehouse(String message) {
        super(message, "Выбранного товара на складе осталось меньше, чем добавил в корзину");
    }
}
