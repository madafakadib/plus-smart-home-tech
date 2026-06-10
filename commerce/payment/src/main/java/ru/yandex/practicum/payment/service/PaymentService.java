package ru.yandex.practicum.payment.service;

import jakarta.persistence.EntityNotFoundException; // ИСПРАВЛЕНО: правильный импорт для JPA
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.api.order.OrderDto;
import ru.yandex.practicum.api.clients.OrderServiceClient;
import ru.yandex.practicum.api.order.PaymentResponseDto;
import ru.yandex.practicum.api.shoppingstore.ProductDto;
import ru.yandex.practicum.api.clients.ShoppingStoreClient;
import ru.yandex.practicum.payment.model.Mapper;
import ru.yandex.practicum.payment.model.Payment;
import ru.yandex.practicum.payment.model.PaymentStatus;
import ru.yandex.practicum.payment.repository.PaymentRepository;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderServiceClient orderServiceClient;
    private final ShoppingStoreClient shoppingStoreClient;

    public PaymentService(PaymentRepository paymentRepository,
                          OrderServiceClient orderServiceClient,
                          ShoppingStoreClient shoppingStoreClient) {
        this.paymentRepository = paymentRepository;
        this.orderServiceClient = orderServiceClient;
        this.shoppingStoreClient = shoppingStoreClient;
    }

    private String cleanId(String id) {
        return id != null ? id.replace("\"", "").trim() : null;
    }

    @Transactional
    public PaymentResponseDto createPayment(OrderDto orderDto) {
        if (orderDto == null || orderDto.getOrderId() == null || orderDto.getProducts() == null || orderDto.getProducts().isEmpty()) {
            throw new IllegalArgumentException("Недостаточно информации в заказе для расчёта");
        }

        BigDecimal productPrice = calculateProductsPrice(orderDto.getProducts());
        BigDecimal taxPrice = productPrice.multiply(BigDecimal.valueOf(0.10));
        BigDecimal deliveryPrice = orderDto.getDeliveryPrice() != null
                ? orderDto.getDeliveryPrice()
                : BigDecimal.valueOf(50.0);

        BigDecimal totalPrice = productPrice.add(taxPrice).add(deliveryPrice);

        Payment payment = Mapper.toEntity(orderDto, productPrice, taxPrice, deliveryPrice, totalPrice);
        Payment savedPayment = paymentRepository.save(payment);

        return Mapper.toResponseDto(savedPayment);
    }

    public BigDecimal calculateTotalOrderPrice(OrderDto orderDto) {
        if (orderDto == null || orderDto.getProducts() == null || orderDto.getProducts().isEmpty()) {
            throw new IllegalArgumentException("Недостаточно информации в заказе для расчёта");
        }

        BigDecimal productPrice = calculateProductsPrice(orderDto.getProducts());
        BigDecimal taxPrice = productPrice.multiply(BigDecimal.valueOf(0.10));
        BigDecimal productWithTax = productPrice.add(taxPrice);
        BigDecimal deliveryPrice = orderDto.getDeliveryPrice() != null
                ? orderDto.getDeliveryPrice()
                : BigDecimal.valueOf(50.0);

        return productWithTax.add(deliveryPrice);
    }

    @Transactional
    public void setPaymentSuccess(String paymentId) {
        Payment payment = paymentRepository.findById(cleanId(paymentId))
                .orElseThrow(() -> new EntityNotFoundException("Заказ не найден"));

        payment.setStatus(PaymentStatus.SUCCESS);
        paymentRepository.save(payment);

        orderServiceClient.notifyPaymentSuccess(payment.getOrderId());
    }

    public BigDecimal calculateOnlyProductsPrice(OrderDto orderDto) {
        if (orderDto == null || orderDto.getProducts() == null || orderDto.getProducts().isEmpty()) {
            throw new IllegalArgumentException("Недостаточно информации в заказе для расчёта");
        }

        return calculateProductsPrice(orderDto.getProducts());
    }

    @Transactional
    public void setPaymentFailed(String paymentId) {
        Payment payment = paymentRepository.findById(cleanId(paymentId))
                .orElseThrow(() -> new EntityNotFoundException("Заказ не найден"));

        payment.setStatus(PaymentStatus.FAILED);
        paymentRepository.save(payment);

        orderServiceClient.notifyPaymentFailed(payment.getOrderId());
    }

    private BigDecimal calculateProductsPrice(Map<String, Integer> products) {
        BigDecimal totalProductsPrice = BigDecimal.ZERO;
        for (Map.Entry<String, Integer> entry : products.entrySet()) {
            ProductDto product = shoppingStoreClient.getProduct(entry.getKey());
            BigDecimal price = product.getPrice();
            totalProductsPrice = totalProductsPrice.add(price.multiply(BigDecimal.valueOf(entry.getValue())));
        }
        return totalProductsPrice;
    }
}
