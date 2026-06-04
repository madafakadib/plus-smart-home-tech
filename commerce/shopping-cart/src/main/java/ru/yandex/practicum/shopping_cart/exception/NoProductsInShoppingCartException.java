package ru.yandex.practicum.shopping_cart.exception;

import ru.yandex.practicum.api.exception.BaseApiException;

public class NoProductsInShoppingCartException extends BaseApiException {
    public NoProductsInShoppingCartException(String message) {
        super(message, "Корзина деактивирована. Изменение содержимого невозможно.");
    }
}
