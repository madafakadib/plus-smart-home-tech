package ru.yandex.practicum.api.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.api.shoppingstore.ProductDto;

import java.util.List;

@FeignClient(name = "shoppingstore", path = "/api/v1/shopping-store")
public interface ShoppingStoreClient {
    @GetMapping("/{productId}")
    ProductDto getProduct(@PathVariable("productId") String productId);

    @GetMapping("/products/batch")
    List<ProductDto> getProductsByIds(@RequestParam("ids") List<String> ids);
}
