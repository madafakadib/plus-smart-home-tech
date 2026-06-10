package ru.yandex.practicum.order.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.api.clients.WarehouseClient;
import ru.yandex.practicum.api.clients.ShoppingStoreClient;
import ru.yandex.practicum.api.shoppingcart.ShoppingCartDto;
import ru.yandex.practicum.api.shoppingstore.ProductDto;
import ru.yandex.practicum.order.model.Mapper;
import ru.yandex.practicum.order.model.Order;
import ru.yandex.practicum.order.model.OrderItem;
import ru.yandex.practicum.order.model.OrderState;
import ru.yandex.practicum.order.repository.OrderRepository;
import ru.yandex.practicum.api.order.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final WarehouseClient warehouseClient;
    private final ShoppingStoreClient shoppingStoreClient;

    private String cleanId(String id) {
        return id != null ? id.replace("\"", "").trim() : null;
    }

    @Transactional
    public OrderDto createOrder(CreateOrderRequest request) {
        ShoppingCartDto cart = request.getShoppingCart();

        try {
            warehouseClient.checkAndBookProducts(cart);
        } catch (FeignException.BadRequest | FeignException.Conflict e) {
            throw new IllegalArgumentException("Нет заказываемого товара на складе", e);
        }

        Order order = new Order();
        order.setShoppingCartId(cart.getShoppingCartId());
        order.setState(OrderState.NEW);

        BigDecimal productPriceSum = BigDecimal.ZERO;
        List<OrderItem> items = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : cart.getProducts().entrySet()) {
            String productId = entry.getKey();
            int quantity = entry.getValue();

            ProductDto productDto = shoppingStoreClient.getProduct(productId);
            BigDecimal price = productDto.getPrice();

            productPriceSum = productPriceSum.add(price.multiply(BigDecimal.valueOf(quantity)));

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProductId(productId);
            item.setQuantity(quantity);
            item.setPrice(price);
            items.add(item);
        }

        order.setItems(items);
        order.setItemsPrice(productPriceSum);
        order.setTotalPrice(productPriceSum);

        order.setTotalWeight(10.0);
        order.setTotalVolume(10.0);
        order.setFragile(true);

        Order savedOrder = orderRepository.save(order);
        return Mapper.toDto(savedOrder);
    }

    @Transactional
    public OrderDto handlePaymentError(String orderId) {
        Order order = orderRepository.findById(cleanId(orderId))
                .orElseThrow(() -> new IllegalArgumentException("Не найден заказ"));

        order.setState(OrderState.PAYMENT_FAILED);
        Order updatedOrder = orderRepository.save(order);
        return Mapper.toDto(updatedOrder);
    }

    @Transactional
    public OrderDto deliveryOrder(String orderId) {
        Order order = orderRepository.findById(cleanId(orderId))
                .orElseThrow(() -> new IllegalArgumentException("Не найден заказ"));

        String mockDeliveryId = UUID.randomUUID().toString();
        order.setDeliveryId(mockDeliveryId);
        order.setState(OrderState.ON_DELIVERY);

        Order updatedOrder = orderRepository.save(order);
        return Mapper.toDto(updatedOrder);
    }

    @Transactional
    public OrderDto handleDeliveryFailed(String orderId) {
        Order order = orderRepository.findById(cleanId(orderId))
                .orElseThrow(() -> new IllegalArgumentException("Не найден заказ"));

        order.setState(OrderState.DELIVERY_FAILED);
        Order updatedOrder = orderRepository.save(order);
        return Mapper.toDto(updatedOrder);
    }

    @Transactional
    public OrderDto completeOrder(String orderId) {
        Order order = orderRepository.findById(cleanId(orderId))
                .orElseThrow(() -> new IllegalArgumentException("Не найден заказ"));

        order.setState(OrderState.COMPLETED);
        Order updatedOrder = orderRepository.save(order);
        return Mapper.toDto(updatedOrder);
    }

    @Transactional
    public OrderDto payOrder(String orderId) {
        Order order = orderRepository.findById(cleanId(orderId))
                .orElseThrow(() -> new IllegalArgumentException("Не найден заказ"));

        String mockPaymentId = UUID.randomUUID().toString();
        order.setPaymentId(mockPaymentId);
        order.setState(OrderState.PAID);

        Order updatedOrder = orderRepository.save(order);
        return Mapper.toDto(updatedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderDto> getOrdersByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя пользователя не должно быть пустым");
        }

        List<Order> orders = orderRepository.findAllByUsername(username);
        return orders.stream()
                .map(Mapper::toDto)
                .toList();
    }

    @Transactional
    public OrderDto returnOrder(ProductReturnRequest request) {
        Order order = orderRepository.findById(cleanId(request.getOrderId()))
                .orElseThrow(() -> new IllegalArgumentException("Не найден заказ"));

        try {
            warehouseClient.returnProducts(request);
        } catch (FeignException e) {
            throw new IllegalStateException("Ошибка при взаимодействии со складом во время возврата", e);
        }

        order.setState(OrderState.PRODUCT_RETURNED);
        Order updatedOrder = orderRepository.save(order);
        return Mapper.toDto(updatedOrder);
    }

    @Transactional
    public OrderDto calculateTotal(String orderId) {
        Order order = orderRepository.findById(cleanId(orderId))
                .orElseThrow(() -> new IllegalArgumentException("Не найден заказ"));

        BigDecimal productPriceSum = BigDecimal.ZERO;
        for (OrderItem item : order.getItems()) {
            productPriceSum = productPriceSum.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        order.setItemsPrice(productPriceSum);

        BigDecimal deliveryPrice = order.getDeliveryPrice() != null ? order.getDeliveryPrice() : BigDecimal.ZERO;
        order.setTotalPrice(productPriceSum.add(deliveryPrice));

        Order updatedOrder = orderRepository.save(order);
        return Mapper.toDto(updatedOrder);
    }

    @Transactional
    public OrderDto calculateDelivery(String orderId) {
        Order order = orderRepository.findById(cleanId(orderId))
                .orElseThrow(() -> new IllegalArgumentException("Не найден заказ"));

        BigDecimal calculatedDeliveryPrice = BigDecimal.valueOf(250.0);
        order.setDeliveryPrice(calculatedDeliveryPrice);

        BigDecimal productPrice = order.getItemsPrice() != null ? order.getItemsPrice() : BigDecimal.ZERO;
        order.setTotalPrice(productPrice.add(calculatedDeliveryPrice));

        Order updatedOrder = orderRepository.save(order);
        return Mapper.toDto(updatedOrder);
    }

    @Transactional
    public OrderDto assembleOrder(String orderId) {
        Order order = orderRepository.findById(cleanId(orderId))
                .orElseThrow(() -> new IllegalArgumentException("Не найден заказ"));

        order.setState(OrderState.ASSEMBLED);
        Order updatedOrder = orderRepository.save(order);
        return Mapper.toDto(updatedOrder);
    }

    @Transactional
    public OrderDto handleAssemblyFailed(String orderId) {
        Order order = orderRepository.findById(cleanId(orderId))
                .orElseThrow(() -> new IllegalArgumentException("Не найден заказ"));

        order.setState(OrderState.ASSEMBLY_FAILED);
        Order updatedOrder = orderRepository.save(order);
        return Mapper.toDto(updatedOrder);
    }
}
