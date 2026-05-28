package ru.yandex.practicum.shopping_cart.mapper;

import ru.yandex.practicum.api.shoppingcart.ShoppingCartDto;
import ru.yandex.practicum.shopping_cart.entity.CartItem;
import ru.yandex.practicum.shopping_cart.entity.ShoppingCart;

import java.util.stream.Collectors;

public class Mapper {
    public static ShoppingCartDto toDto(ShoppingCart shoppingCart) {
        return ShoppingCartDto
                .builder()
                .shoppingCartId(shoppingCart.getShoppingCartId())
                .products(shoppingCart
                        .getItems()
                        .stream()
                        .collect(Collectors
                                .toMap(CartItem::getProductId,
                                        CartItem::getQuantity)))
                .build();
    }
}
