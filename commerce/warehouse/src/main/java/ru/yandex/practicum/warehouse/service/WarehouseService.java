package ru.yandex.practicum.warehouse.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.api.shoppingcart.ShoppingCartDto;
import ru.yandex.practicum.api.warehouse.*;
import ru.yandex.practicum.warehouse.entity.DimensionEmbeddable;
import ru.yandex.practicum.warehouse.entity.OrderBooking;
import ru.yandex.practicum.warehouse.entity.WarehouseProduct;
import ru.yandex.practicum.warehouse.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.warehouse.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.warehouse.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.warehouse.repository.OrderBookingRepository;
import ru.yandex.practicum.warehouse.repository.WarehouseRepository;

import java.security.SecureRandom;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final OrderBookingRepository orderBookingRepository;

    private static final String[] ADDRESSES = new String[]{"ADDRESS_1", "ADDRESS_2"};
    private static final String CURRENT_ADDRESS = ADDRESSES[Random.from(new SecureRandom()).nextInt(0, ADDRESSES.length)];

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

        Set<String> productIds = shoppingCart.getProducts().keySet();
        List<WarehouseProduct> dbProducts = warehouseRepository.findAllById(productIds);
        Map<String, WarehouseProduct> warehouseProductMap = dbProducts.stream()
                .collect(Collectors.toMap(WarehouseProduct::getProductId, Function.identity()));

        for (Map.Entry<String, Integer> item : shoppingCart.getProducts().entrySet()) {
            String productId = item.getKey();
            Integer requestedQuantity = item.getValue();

            WarehouseProduct product = warehouseProductMap.get(productId);
            if (product == null || product.getQuantity() < requestedQuantity) {
                missingProducts.add(productId);
            } else {
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

    @Transactional
    public void linkDeliveryToOrder(ShipmentRequest request) {
        if (request == null || request.getOrderId() == null || request.getDeliveryId() == null) {
            throw new IllegalArgumentException("Данные запроса на отгрузку не могут быть пустыми");
        }

        List<OrderBooking> bookings = orderBookingRepository.findAllByOrderId(request.getOrderId());
        if (bookings.isEmpty()) {
            throw new EntityNotFoundException("Не найдены забронированные товары для заказа: " + request.getOrderId());
        }

        for (OrderBooking booking : bookings) {
            booking.setDeliveryId(request.getDeliveryId());
            orderBookingRepository.save(booking);
        }
    }

    @Transactional
    public void returnProductsToStock(Map<String, Integer> products) {
        if (products == null || products.isEmpty()) {
            return;
        }

        Set<String> productIds = products.entrySet().stream()
                .filter(entry -> entry.getValue() > 0)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        if (productIds.isEmpty()) {
            return;
        }

        List<WarehouseProduct> existingProducts = warehouseRepository.findAllById(productIds);
        Map<String, WarehouseProduct> productMap = existingProducts.stream()
                .collect(Collectors.toMap(WarehouseProduct::getProductId, Function.identity()));

        List<WarehouseProduct> productsToSave = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : products.entrySet()) {
            String productId = entry.getKey();
            int returnQuantity = entry.getValue();

            if (returnQuantity <= 0) {
                continue;
            }
            WarehouseProduct product = productMap.getOrDefault(productId,
                    WarehouseProduct.builder()
                            .productId(productId)
                            .quantity(0L)
                            .dimension(new DimensionEmbeddable(0.0, 0.0, 0.0))
                            .weight(0.0)
                            .fragile(false)
                            .build()
            );
            product.setQuantity(product.getQuantity() + returnQuantity);
            productsToSave.add(product);
        }
        warehouseRepository.saveAll(productsToSave);
    }


    @Transactional
    public BookedProductsDto assembleProductsForOrder(WarehouseAssemblyRequest request) {
        if (request == null || request.getOrderId() == null || request.getProducts() == null || request.getProducts().isEmpty()) {
            throw new IllegalArgumentException("Недостаточно информации в запросе для сборки заказа");
        }

        double totalWeight = 0.0;
        double totalVolume = 0.0;
        boolean hasFragile = false;

        Set<String> productIds = request.getProducts().keySet();
        List<WarehouseProduct> dbProducts = warehouseRepository.findAllById(productIds);

        Map<String, WarehouseProduct> warehouseProductMap = dbProducts.stream()
                .collect(Collectors.toMap(WarehouseProduct::getProductId, java.util.function.Function.identity()));

        List<WarehouseProduct> productsToSave = new ArrayList<>();
        List<OrderBooking> bookingsToSave = new ArrayList<>();

        for (Map.Entry<String, Integer> item : request.getProducts().entrySet()) {
            String productId = item.getKey();
            Integer requestedQuantity = item.getValue();

            WarehouseProduct product = warehouseProductMap.get(productId);

            if (product == null || product.getQuantity() < requestedQuantity) {
                throw new ProductInShoppingCartLowQuantityInWarehouse(
                        "Ошибка, товар " + productId + " не находится в требуемом количестве на складе"
                );
            }

            product.setQuantity(product.getQuantity() - requestedQuantity);
            productsToSave.add(product);

            OrderBooking booking = OrderBooking.builder()
                    .id(UUID.randomUUID().toString())
                    .orderId(request.getOrderId())
                    .productId(productId)
                    .quantity(requestedQuantity)
                    .build();
            bookingsToSave.add(booking);

            totalWeight += product.getWeight() * requestedQuantity;

            double singleVolume = product.getDimension().getWidth()
                    * product.getDimension().getHeight()
                    * product.getDimension().getDepth();
            totalVolume += singleVolume * requestedQuantity;

            if (product.isFragile()) {
                hasFragile = true;
            }
        }

        warehouseRepository.saveAll(productsToSave);
        orderBookingRepository.saveAll(bookingsToSave);

        return new BookedProductsDto(totalWeight, totalVolume, hasFragile);
    }


}
