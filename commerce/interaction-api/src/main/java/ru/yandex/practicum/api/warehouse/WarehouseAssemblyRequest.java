package ru.yandex.practicum.api.warehouse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseAssemblyRequest {
    private String orderId;
    private Map<String, Integer> products;
}
