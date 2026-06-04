package ru.yandex.practicum.api.shoppingstore;

import lombok.*;

@Builder @Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PageableObject {
    Integer offset;
    SortObject sort;
    Boolean unpaged;
    Boolean paged;
    Integer pageNumber;
    Integer pageSize;
}
