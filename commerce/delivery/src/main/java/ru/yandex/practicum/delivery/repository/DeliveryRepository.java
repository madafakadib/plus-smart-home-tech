package ru.yandex.practicum.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.delivery.model.Delivery;

import java.util.Optional;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, String> {
    Optional<Delivery> findByOrderId(String orderId);
}
