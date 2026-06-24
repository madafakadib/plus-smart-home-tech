package ru.yandex.practicum.delivery.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.api.clients.WarehouseClient;
import ru.yandex.practicum.api.delivery.DeliveryDto;
import ru.yandex.practicum.api.clients.OrderServiceClient;
import ru.yandex.practicum.api.warehouse.ShipmentRequest;
import ru.yandex.practicum.delivery.model.Delivery;
import ru.yandex.practicum.delivery.model.DeliveryStatus;
import ru.yandex.practicum.delivery.model.Mapper;
import ru.yandex.practicum.delivery.repository.DeliveryRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final OrderServiceClient orderServiceClient;
    private final WarehouseClient warehouseClient;

    public DeliveryService(DeliveryRepository deliveryRepository,
                           OrderServiceClient orderServiceClient,
                           WarehouseClient warehouseClient) {
        this.deliveryRepository = deliveryRepository;
        this.orderServiceClient = orderServiceClient;
        this.warehouseClient = warehouseClient;
    }

    private String cleanId(String id) {
        return id != null ? id.replace("\"", "").trim() : null;
    }

    @Transactional
    public DeliveryDto createDelivery(DeliveryDto deliveryDto) {
        if (deliveryDto == null || deliveryDto.getOrderId() == null) {
            throw new IllegalArgumentException("Недостаточно информации для оформления доставки");
        }

        Delivery delivery = Mapper.toEntity(deliveryDto);
        Delivery savedDelivery = deliveryRepository.save(delivery);

        return Mapper.toDto(savedDelivery);
    }

    @Transactional
    public DeliveryDto emulateDeliverySuccess(String orderId) {
        Delivery delivery = deliveryRepository.findByOrderId(cleanId(orderId))
                .orElseThrow(() -> new EntityNotFoundException("Не найдена доставка"));

        delivery.setStatus(DeliveryStatus.DELIVERED);
        Delivery updatedDelivery = deliveryRepository.save(delivery);

        return Mapper.toDto(updatedDelivery);
    }

    @Transactional
    public DeliveryDto emulateAcceptDelivery(String orderId) {
        Delivery delivery = deliveryRepository.findByOrderId(cleanId(orderId))
                .orElseThrow(() -> new EntityNotFoundException("Не найдена доставка для выдачи"));

        delivery.setStatus(DeliveryStatus.IN_PROGRESS);
        Delivery updatedDelivery = deliveryRepository.save(delivery);

        orderServiceClient.notifyOrderAssembled(delivery.getOrderId());

        ShipmentRequest shipmentRequest = new ShipmentRequest(delivery.getOrderId(), delivery.getId());
        warehouseClient.shippedToDelivery(shipmentRequest);

        return Mapper.toDto(updatedDelivery);
    }

    @Transactional
    public DeliveryDto emulateDeliveryFailed(String orderId) {
        Delivery delivery = deliveryRepository.findByOrderId(cleanId(orderId))
                .orElseThrow(() -> new EntityNotFoundException("Не найдена доставка для сбоя"));

        delivery.setStatus(DeliveryStatus.FAILED);
        Delivery updatedDelivery = deliveryRepository.save(delivery);

        orderServiceClient.notifyDeliveryFailed(delivery.getOrderId());

        return Mapper.toDto(updatedDelivery);
    }

    public BigDecimal calculateDeliveryCost(ru.yandex.practicum.api.order.OrderDto orderDto) {
        if (orderDto == null || orderDto.getOrderId() == null) {
            throw new IllegalArgumentException("Идентификатор заказа не может быть пустым");
        }

        Delivery delivery = deliveryRepository.findByOrderId(cleanId(orderDto.getOrderId()))
                .orElseThrow(() -> new EntityNotFoundException("Не найдена доставка для расчёта"));

        BigDecimal currentSum = BigDecimal.valueOf(5.0);

        String warehouseStreet = delivery.getFromAddress() != null ? delivery.getFromAddress().getStreet() : "";

        if (warehouseStreet.contains("ADDRESS_1")) {
            currentSum = currentSum.multiply(BigDecimal.valueOf(1.0));
        } else if (warehouseStreet.contains("ADDRESS_2")) {
            BigDecimal product = currentSum.multiply(BigDecimal.valueOf(2.0));
            currentSum = product.add(BigDecimal.valueOf(5.0));
        }

        if (orderDto.getFragile() != null && orderDto.getFragile()) {
            BigDecimal surge = currentSum.multiply(BigDecimal.valueOf(0.2));
            currentSum = currentSum.add(surge);
        }

        double weight = orderDto.getDeliveryWeight() != null ? orderDto.getDeliveryWeight() : 0.0;
        BigDecimal weightSurge = BigDecimal.valueOf(weight).multiply(BigDecimal.valueOf(0.3));
        currentSum = currentSum.add(weightSurge);

        double volume = orderDto.getDeliveryVolume() != null ? orderDto.getDeliveryVolume() : 0.0;
        BigDecimal volumeSurge = BigDecimal.valueOf(volume).multiply(BigDecimal.valueOf(0.2));
        currentSum = currentSum.add(volumeSurge);

        String toStreet = delivery.getToAddress() != null ? delivery.getToAddress().getStreet() : null;

        boolean sameStreet = toStreet != null && !warehouseStreet.isBlank() && warehouseStreet.contains(toStreet);

        if (!sameStreet) {
            BigDecimal addressSurge = currentSum.multiply(BigDecimal.valueOf(0.2));
            currentSum = currentSum.add(addressSurge);
        }

        return currentSum.setScale(2, RoundingMode.HALF_UP);
    }
}
