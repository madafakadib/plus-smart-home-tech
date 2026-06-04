package ru.yandex.practicum.api.shoppingcart;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Map;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartDto {

    @NotNull(message = "Идентификатор корзины обязателен")
    private String shoppingCartId;


    @NotNull(message = "Список товаров не может быть null")
    private Map<String, Integer> products;
}
