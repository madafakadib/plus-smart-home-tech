package ru.yandex.practicum.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.warehouse.entity.OrderBooking;

import java.util.List;

public interface OrderBookingRepository extends JpaRepository<OrderBooking, String> {
    List<OrderBooking> findAllByOrderId(String orderId);
}
