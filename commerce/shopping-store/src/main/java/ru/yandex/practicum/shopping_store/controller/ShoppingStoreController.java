package ru.yandex.practicum.shopping_store.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.api.shoppingstore.*;
import ru.yandex.practicum.shopping_store.service.ShoppingStoreService;

import java.util.List;


@RestController
@RequestMapping("/api/v1/shopping-store")
public class ShoppingStoreController {

    private final ShoppingStoreService shoppingStoreService;

    public ShoppingStoreController(ShoppingStoreService shoppingStoreService) {
        this.shoppingStoreService = shoppingStoreService;
    }

    @GetMapping
    public PageProductDto getProducts(
            @RequestParam("category") ProductCategory productCategory,
            Pageable pageable
    ) {
        return shoppingStoreService.getProductsByCategory(productCategory, pageable);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto createProduct(@RequestBody ProductDto productDto) {
        return shoppingStoreService.createProduct(productDto);
    }

    @PostMapping
    public ProductDto updateProduct(@RequestBody ProductDto productDto) {
        return shoppingStoreService.updateProduct(productDto);
    }

    @PostMapping(path = "/removeProductFromStore")
    public Boolean deactivateProduct(@RequestBody String productId) {
        return shoppingStoreService.deactivateProduct(productId);
    }

    @PostMapping(path = "/quantityState")
    public Boolean updateQuantity(@RequestParam String productId,
                                  @RequestParam QuantityState quantityState) {
        return shoppingStoreService.updateQuantity(productId, quantityState);
    }

    @GetMapping(path = "/{productId}")
    public ProductDto getProductById(@PathVariable("productId") String productId) {
        return shoppingStoreService.getProductById(productId);
    }

    @GetMapping(path = "/products/batch")
    public List<ProductDto> getProductsByIds(@RequestParam("ids") List<String> ids) {
        return shoppingStoreService.findAllByIds(ids);
    }
}
