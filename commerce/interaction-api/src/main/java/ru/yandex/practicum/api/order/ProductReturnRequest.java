package ru.yandex.practicum.api.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductReturnRequest {
    private String orderId;
    private Map<String, Integer> products;
}
