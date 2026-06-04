package ru.yandex.practicum.warehouse.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.api.shoppingcart.ShoppingCartDto;
import ru.yandex.practicum.api.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.api.warehouse.AddressDto;
import ru.yandex.practicum.api.warehouse.BookedProductsDto;
import ru.yandex.practicum.api.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.warehouse.service.WarehouseService;

@RestController
@RequestMapping("/api/v1/warehouse")
public class WarehouseController {

    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @PutMapping
    public void createProduct(@Valid @RequestBody NewProductInWarehouseRequest request) {
        warehouseService.createProduct(request);
    }

    @PostMapping("/check")
    public BookedProductsDto checkShoppingCart(@Valid @RequestBody ShoppingCartDto shoppingCart) {
        return warehouseService.checkShoppingCart(shoppingCart);
    }

    @PostMapping("/add")
    public void confirmProduct(@Valid @RequestBody AddProductToWarehouseRequest request) {
        warehouseService.confirmProduct(request);
    }

    @GetMapping("/address")
    public AddressDto getAddress() {
        return warehouseService.getAddress();
    }
}
