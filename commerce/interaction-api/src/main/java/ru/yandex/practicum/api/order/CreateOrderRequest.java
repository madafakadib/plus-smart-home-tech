package ru.yandex.practicum.api.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.api.shoppingcart.ShoppingCartDto;
import ru.yandex.practicum.api.warehouse.AddressDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    private ShoppingCartDto shoppingCart;
    private AddressDto deliveryAddress;
}
