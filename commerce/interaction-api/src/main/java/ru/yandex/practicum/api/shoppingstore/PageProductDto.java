package ru.yandex.practicum.api.shoppingstore;

import lombok.*;

import java.util.List;

@Builder @Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PageProductDto {
    Integer totalElement;
    Integer totalPages;
    Boolean first;
    Boolean last;
    Integer size;
    List<ProductDto> content;
    Integer number;
    List<SortObject> sort;
    Integer numberOfElements;
    PageableObject pageable;
    Boolean empty;
}
