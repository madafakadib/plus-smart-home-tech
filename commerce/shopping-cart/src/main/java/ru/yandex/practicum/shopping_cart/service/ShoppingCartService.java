package ru.yandex.practicum.shopping_cart.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.api.shoppingcart.ChangeProductQuantityRequest;
import ru.yandex.practicum.api.shoppingcart.ShoppingCartDto;
import ru.yandex.practicum.api.shoppingstore.ProductDto;
import ru.yandex.practicum.shopping_cart.entity.CartItem;
import ru.yandex.practicum.shopping_cart.entity.ShoppingCart;
import ru.yandex.practicum.shopping_cart.exception.NoProductsInShoppingCartException;
import ru.yandex.practicum.shopping_cart.exception.NotFoundCartException;
import ru.yandex.practicum.shopping_cart.mapper.Mapper;
import ru.yandex.practicum.shopping_cart.repository.ShoppingCartRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductServiceClient productServiceClient;
    private final WarehouseServiceClient warehouseServiceClient;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, ProductServiceClient productServiceClient, WarehouseServiceClient warehouseServiceClient) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.productServiceClient = productServiceClient;
        this.warehouseServiceClient = warehouseServiceClient;
    }

    public ShoppingCartDto getCart(String username) {
        ShoppingCart cart = shoppingCartRepository.findByUsername(username)
                .orElseGet(() -> {
                    ShoppingCart newCart = new ShoppingCart();
                    newCart.setShoppingCartId(UUID.randomUUID().toString());
                    newCart.setUsername(username);
                    return shoppingCartRepository.save(newCart);
                });

        return Mapper.toDto(cart);
    }

    public ShoppingCartDto addItem(String username, Map<String, Integer> productsMap) {
        ShoppingCart cart = shoppingCartRepository.findByUsername(username)
                .orElseGet(() -> {
                    ShoppingCart newCart = new ShoppingCart();
                    newCart.setShoppingCartId(UUID.randomUUID().toString());
                    newCart.setUsername(username);
                    return shoppingCartRepository.save(newCart);
                });

        for (Map.Entry<String, Integer> entry : productsMap.entrySet()) {
            Integer quantity = entry.getValue();

            Optional<CartItem> existingItem = cart.getItems().stream()
                    .findFirst();

            if (existingItem.isPresent()) {
                CartItem item = existingItem.get();
                item.setQuantity(item.getQuantity() + quantity);
            } else {
                CartItem newItem = CartItem.builder()
                        .shoppingCart(cart)
                        .productId(entry.getKey())
                        .quantity(quantity)
                        .build();
                cart.getItems().add(newItem);
            }
        }

        ShoppingCart savedCart = shoppingCartRepository.save(cart);
        ShoppingCartDto dto = Mapper.toDto(savedCart);

        warehouseServiceClient.checkAndBookProducts(dto);

        return dto;
    }


    public ShoppingCartDto removeItems(String username, List<String> productId) {
        ShoppingCart cart = shoppingCartRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundCartException("Корзина пользователя не найдена"));

        boolean anyProductExists = cart.getItems().stream()
                .anyMatch(item -> productId.contains(item.getProductId()));

        if (!anyProductExists) {
            throw new NoProductsInShoppingCartException("Нет данных товаров в корзине");
        }

        cart.getItems().removeIf(item -> productId.contains(item.getProductId()));

        ShoppingCart savedCart = shoppingCartRepository.save(cart);
        return Mapper.toDto(savedCart);
    }

    public ShoppingCartDto changeProductQuantity(String username, ChangeProductQuantityRequest changeProductQuantityRequest) {
        ShoppingCart cart = shoppingCartRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundCartException("Корзина пользователя не найдена"));

        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(changeProductQuantityRequest.getProductId()))
                .findFirst()
                .orElseThrow(() -> new NoProductsInShoppingCartException("Нет данного товара в корзине"));

        existingItem.setQuantity(changeProductQuantityRequest.getNewQuantity());

        ShoppingCart savedCart = shoppingCartRepository.save(cart);
        ShoppingCartDto dto = Mapper.toDto(savedCart);

        warehouseServiceClient.checkAndBookProducts(dto);

        return dto;
    }

    public void deactivateCart(String username) {
        ShoppingCart cart = shoppingCartRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundCartException("Корзина пользователя не найдена"));
        shoppingCartRepository.delete(cart);
    }
}
