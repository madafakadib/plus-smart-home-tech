package ru.yandex.practicum.shopping_cart.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.api.exception.ApiException;

import java.util.Collections;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoProductsInShoppingCartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiException handleNoProductsInCart(NoProductsInShoppingCartException ex) {
        return ApiException.builder()
                .userMessage("Корзина деактивирована. Изменение содержимого невозможно.")
                .message(ex.getMessage())
                .suppressed(Collections.emptyList())
                .localizedMessage(ex.getLocalizedMessage())
                .build();
    }

    @ExceptionHandler(NotAuthorizedUserRuntimeException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiException handleNotAuthorized(NotAuthorizedUserRuntimeException ex) {
        return ApiException.builder()
                .userMessage("Пользователь не авторизован.")
                .message(ex.getMessage())
                .suppressed(Collections.emptyList())
                .localizedMessage(ex.getLocalizedMessage())
                .build();
    }

    @ExceptionHandler(NotFoundCartException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiException handleCartNotFound(NotFoundCartException ex) {
        return ApiException.builder()
                .userMessage("Корзина не найдена.")
                .message(ex.getMessage())
                .suppressed(Collections.emptyList())
                .localizedMessage(ex.getLocalizedMessage())
                .build();
    }
}
