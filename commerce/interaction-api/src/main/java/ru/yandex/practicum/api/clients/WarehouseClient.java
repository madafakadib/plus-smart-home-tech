package ru.yandex.practicum.api.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.api.order.ProductReturnRequest;
import ru.yandex.practicum.api.shoppingcart.ShoppingCartDto;
import ru.yandex.practicum.api.warehouse.AddressDto;
import ru.yandex.practicum.api.warehouse.BookedProductsDto;
import ru.yandex.practicum.api.warehouse.ShipmentRequest;
import ru.yandex.practicum.api.warehouse.WarehouseAssemblyRequest;

@FeignClient(name = "warehouse", path = "/api/v1/warehouse")
public interface WarehouseClient {

    @PostMapping("/assembly")
    BookedProductsDto assemblyProductForOrderFromShoppingCart(@RequestBody WarehouseAssemblyRequest request);

    @GetMapping("/address")
    AddressDto getWarehouseAddress();

    @PostMapping("/check")
    void checkAndBookProducts(@RequestBody ShoppingCartDto shoppingCart);

    @PostMapping("/return")
    void returnProducts(@RequestBody ProductReturnRequest productReturnRequest);

    @PostMapping("/shipped")
    void shippedToDelivery(@RequestBody ShipmentRequest shipmentRequest);
}
