package ru.yandex.practicum.order.model;

import ru.yandex.practicum.api.order.OrderDto;

import java.util.Map;
import java.util.stream.Collectors;

public class Mapper {

    public static OrderDto toDto(Order order) {
        if (order == null) {
            return null;
        }

        Map<String, Integer> productsMap = order.getItems().stream()
                .collect(Collectors.toMap(
                        OrderItem::getProductId,
                        OrderItem::getQuantity,
                        Integer::sum
                ));

        return new OrderDto(
                order.getId(),
                order.getShoppingCartId(),
                productsMap,
                order.getPaymentId(),
                order.getDeliveryId(),
                order.getState() != null ? order.getState().name() : null,
                order.getTotalWeight(),
                order.getTotalVolume(),
                order.getFragile(),
                order.getTotalPrice(),
                order.getDeliveryPrice(),
                order.getItemsPrice()
        );
    }
}
