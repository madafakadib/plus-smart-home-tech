package ru.yandex.practicum.api.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.api.shoppingstore.ProductDto;

@FeignClient(name = "shoppingstore", path = "/api/v1/shopping-store")
public interface ShoppingStoreClient {
    @GetMapping("/{productId}")
    ProductDto getProduct(@PathVariable("productId") String productId);
}
