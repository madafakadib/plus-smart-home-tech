package ru.yandex.practicum.warehouse.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.api.shoppingcart.ShoppingCartDto;
import ru.yandex.practicum.api.warehouse.*;
import ru.yandex.practicum.warehouse.service.WarehouseService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/warehouse")
public class WarehouseController {

    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @PostMapping("/shipped")
    public void linkDeliveryToWarehouse(@RequestBody ShipmentRequest request) {
            warehouseService.linkDeliveryToOrder(request);
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

    @PostMapping("/return")
    public void returnProducts(@RequestBody Map<String, Integer> products) {
        warehouseService.returnProductsToStock(products);
    }

    @PostMapping("/assembly")
    public BookedProductsDto assembleProducts(@RequestBody WarehouseAssemblyRequest request) {
        return warehouseService.assembleProductsForOrder(request);
    }

}
