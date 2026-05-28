package ru.yandex.practicum.shopping_cart.exception;

import org.springframework.http.HttpStatus;
import ru.yandex.practicum.api.exception.BaseApiException;

public class NoProductsInShoppingCartException extends BaseApiException {
    public NoProductsInShoppingCartException(String message) {
        super(message, HttpStatus.BAD_REQUEST, "Корзина деактивирована. Изменение содержимого невозможно.");
    }
}
