package ru.yandex.practicum.shopping_cart.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.api.shoppingcart.ChangeProductQuantityRequest;
import ru.yandex.practicum.api.shoppingcart.ShoppingCartDto;
import ru.yandex.practicum.shopping_cart.service.ShoppingCartService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping
    public ShoppingCartDto getCart(@RequestParam("username") String username) {
        return shoppingCartService.getCart(username);
    }

    @PutMapping
    public ShoppingCartDto addItem(
            @RequestParam("username") String username,
            @RequestBody Map<String, Integer> productsMap) {
        return shoppingCartService.addItem(username, productsMap);
    }

    @DeleteMapping()
    public void deactivateCart(@RequestParam("username") String username) {
        shoppingCartService.deactivateCart(username);
    }

    @PostMapping("/remove")
    public ShoppingCartDto removeItems(@RequestParam("username") String username,
                                       @RequestBody List<String> productId) {
        return shoppingCartService.removeItems(username, productId);
    }

    @PostMapping("/change-quantity")
    public ShoppingCartDto changeProductQuantity(@RequestParam("username") String username,
                                          @RequestBody ChangeProductQuantityRequest changeProductQuantityRequest) {
        return shoppingCartService.changeProductQuantity(username, changeProductQuantityRequest);
    }
}
