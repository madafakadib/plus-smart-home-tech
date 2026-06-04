package ru.yandex.practicum.shopping_store.mapper;

import org.springframework.data.domain.Page;
import ru.yandex.practicum.api.shoppingstore.PageProductDto;
import ru.yandex.practicum.api.shoppingstore.PageableObject;
import ru.yandex.practicum.api.shoppingstore.ProductDto;

import ru.yandex.practicum.api.shoppingstore.SortObject;
import ru.yandex.practicum.shopping_store.entity.Product;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.ASC;

public class Mapper {
    public static ProductDto toDto(Product product) {
        return ProductDto
                .builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .description(product.getDescription())
                .imageSrc(product.getImageSrc())
                .quantityState(product.getQuantityState())
                .productState(product.getProductState())
                .productCategory(product.getProductCategory())
                .price(product.getPrice())
                .build();
    }

    public static Product toEntity(ProductDto productDto) {
        return Product
                .builder()
                .productId(productDto.getProductId())
                .productName(productDto.getProductName())
                .description(productDto.getDescription())
                .imageSrc(productDto.getImageSrc())
                .quantityState(productDto.getQuantityState())
                .productState(productDto.getProductState())
                .productCategory(productDto.getProductCategory())
                .price(productDto.getPrice())
                .build();
    }

    public static PageProductDto toPageProductDto(Page<Product> productPage) {
        List<ProductDto> productDtos = productPage.getContent().stream()
                .map(Mapper::toDto)
                .toList();

        var order = productPage.getSort().stream().findFirst()
                .orElseGet(() -> new org.springframework.data.domain.Sort.Order(ASC, "productId"));

        SortObject sortObject = SortObject.builder()
                .direction(order.getDirection().name())
                .nullHandling(order.getNullHandling().name())
                .ascending(order.isAscending())
                .property(order.getProperty())
                .ignoreCase(order.isIgnoreCase())
                .build();

        PageableObject pageableObject = PageableObject.builder()
                .offset((int) productPage.getPageable().getOffset())
                .sort(sortObject)
                .paged(productPage.getPageable().isPaged())
                .unpaged(productPage.getPageable().isUnpaged())
                .pageNumber(productPage.getPageable().getPageNumber())
                .pageSize(productPage.getPageable().getPageSize())
                .build();

        return PageProductDto
                .builder()
                .totalElement((int) productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .first(productPage.isFirst())
                .last(productPage.isLast())
                .size(productPage.getSize())
                .content(productDtos)
                .number(productPage.getNumber())
                .sort(List.of(sortObject))
                .numberOfElements(productPage.getNumberOfElements())
                .empty(productPage.isEmpty())
                .pageable(pageableObject)
                .build();
    }
}
