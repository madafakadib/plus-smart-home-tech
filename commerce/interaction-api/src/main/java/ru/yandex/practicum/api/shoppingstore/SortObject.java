package ru.yandex.practicum.api.shoppingstore;

import lombok.*;

@Builder @Setter @Getter
@AllArgsConstructor @NoArgsConstructor
public class SortObject {
    String direction;
    String nullHandling;
    Boolean ascending;
    String property;
    Boolean ignoreCase;
}
