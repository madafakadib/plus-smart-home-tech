package ru.yandex.practicum.warehouse.service;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.api.shoppingcart.ShoppingCartDto;
import ru.yandex.practicum.api.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.api.warehouse.AddressDto;
import ru.yandex.practicum.api.warehouse.BookedProductsDto;
import ru.yandex.practicum.api.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.warehouse.entity.DimensionEmbeddable;
import ru.yandex.practicum.warehouse.entity.WarehouseProduct;
import ru.yandex.practicum.warehouse.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.warehouse.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.warehouse.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.warehouse.repository.WarehouseRepository;

import java.security.SecureRandom;
import java.util.*;

@Service
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;

    private static final String[] ADDRESSES = new String[] {"ADDRESS_1", "ADDRESS_2"};
    private static final String CURRENT_ADDRESS = ADDRESSES[Random.from(new SecureRandom()).nextInt(0, ADDRESSES.length)];


    public WarehouseService(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    public void createProduct(@Valid NewProductInWarehouseRequest request) {
        String productId = request.getProductId();

        if (warehouseRepository.existsById(productId)) {
            throw new SpecifiedProductAlreadyInWarehouseException("Товар с ID " + productId + " уже зарегистрирован на складе");
        }

        DimensionEmbeddable dimension = new DimensionEmbeddable(
                request.getDimension().getWidth(),
                request.getDimension().getHeight(),
                request.getDimension().getDepth()
        );

        WarehouseProduct productEntity = WarehouseProduct.builder()
                .productId(productId)
                .fragile(request.isFragile())
                .dimension(dimension)
                .weight(request.getWeight())
                .quantity(0L)
                .build();

        warehouseRepository.save(productEntity);
    }

    public BookedProductsDto checkShoppingCart(ShoppingCartDto shoppingCart) {
        List<String> missingProducts = new ArrayList<>();

        double totalWeight = 0.0;
        double totalVolume = 0.0;
        boolean hasFragile = false;

        for (Map.Entry<String, Integer> item : shoppingCart.getProducts().entrySet()) {
            String productId = item.getKey();
            Integer requestedQuantity = item.getValue();

            Optional<WarehouseProduct> productOpt = warehouseRepository.findById(productId);

            if (productOpt.isEmpty() || productOpt.get().getQuantity() < requestedQuantity) {
                missingProducts.add(productId);
            } else {
                WarehouseProduct product = productOpt.get();

                totalWeight += product.getWeight() * requestedQuantity;

                double singleVolume = product.getDimension().getWidth()
                        * product.getDimension().getHeight()
                        * product.getDimension().getDepth();
                totalVolume += singleVolume * requestedQuantity;

                if (product.isFragile()) {
                    hasFragile = true;
                }
            }
        }

        if (!missingProducts.isEmpty()) {
            String missingIdsString = missingProducts.toString();

            throw new ProductInShoppingCartLowQuantityInWarehouse(
                    "На складе недостаточно товаров для оформления заказа. Отсутствуют ID: " + missingIdsString
            );
        }

        return new BookedProductsDto(totalWeight, totalVolume, hasFragile);
    }

    public void confirmProduct(AddProductToWarehouseRequest request) {
        WarehouseProduct product = warehouseRepository.findById(request.getProductId())
                .orElseThrow(() -> new NoSpecifiedProductInWarehouseException("Товар с ID " + request.getProductId() + " не найден на складе"));

        product.setQuantity(product.getQuantity() + request.getQuantity());
        warehouseRepository.save(product);
    }

    public AddressDto getAddress() {
        return new AddressDto(
                CURRENT_ADDRESS,
                CURRENT_ADDRESS,
                CURRENT_ADDRESS,
                CURRENT_ADDRESS,
                CURRENT_ADDRESS
        );
    }
}
