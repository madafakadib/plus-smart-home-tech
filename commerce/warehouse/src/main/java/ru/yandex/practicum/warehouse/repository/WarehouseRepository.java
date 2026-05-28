package ru.yandex.practicum.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.warehouse.entity.WarehouseProduct;

public interface WarehouseRepository extends JpaRepository<WarehouseProduct, String> {
}
