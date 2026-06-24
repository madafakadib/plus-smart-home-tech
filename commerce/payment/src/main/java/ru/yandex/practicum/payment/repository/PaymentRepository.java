package ru.yandex.practicum.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.payment.model.Payment;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, String> {
    Optional<Payment> findByOrderId(String orderId);
}
