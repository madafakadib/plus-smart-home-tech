package ru.yandex.practicum.api.warehouse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentRequest {
    private String orderId;
    private String deliveryId;
}
