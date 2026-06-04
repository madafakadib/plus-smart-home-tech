package ru.yandex.practicum.shopping_cart.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.api.shoppingcart.ShoppingCartDto;

@FeignClient(name = "warehouse", path = "/api/v1/warehouse")
public interface WarehouseServiceClient {

    @PostMapping("/check")
    void checkAndBookProducts(@RequestBody ShoppingCartDto shoppingCart);
}
