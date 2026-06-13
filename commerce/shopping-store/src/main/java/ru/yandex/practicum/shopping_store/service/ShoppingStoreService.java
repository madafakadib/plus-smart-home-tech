package ru.yandex.practicum.shopping_store.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.api.shoppingstore.*;
import ru.yandex.practicum.shopping_store.entity.Product;
import ru.yandex.practicum.shopping_store.exception.ProductNotFoundRuntimeException;
import ru.yandex.practicum.shopping_store.mapper.Mapper;
import ru.yandex.practicum.shopping_store.repository.ProductRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShoppingStoreService {

    private final ProductRepository productRepository;

    public PageProductDto getProductsByCategory(ProductCategory category, Pageable pageable) {
        var productPage = productRepository.findByProductCategoryAndProductState(category, ProductState.ACTIVE, pageable);
        return Mapper.toPageProductDto(productPage);
    }

    public ProductDto createProduct(ProductDto productDto) {
        UUID productId = UUID.randomUUID();
        productDto.setProductId(productId.toString());
        var savedProduct = productRepository.save(Mapper.toEntity(productDto));
        return Mapper.toDto(savedProduct);
    }

    public ProductDto updateProduct(ProductDto productDto) {
        Product product = productRepository.findById(productDto.getProductId())
                .orElseThrow(() -> new ProductNotFoundRuntimeException("Product not found with id: " + productDto.getProductId()));

        product.setProductName(productDto.getProductName());
        product.setDescription(productDto.getDescription());
        product.setImageSrc(productDto.getImageSrc());
        product.setProductCategory(productDto.getProductCategory());
        product.setQuantityState(productDto.getQuantityState());
        product.setPrice(productDto.getPrice());

        var updatedProduct = productRepository.save(product);
        return Mapper.toDto(updatedProduct);
    }

    public Boolean deactivateProduct(String productId) {
        String cleanProductId = productId.replace("\"", "").trim();
        var product = productRepository.findById(cleanProductId);
        if (product.isPresent()) {
            Product updatedProduct = product.get();
            updatedProduct.setProductState(ProductState.DEACTIVATE);
            productRepository.save(updatedProduct);
            return true;
        } else {
            return false;
        }
    }

    public Boolean updateQuantity(String productId, QuantityState quantityState) {
        var product = productRepository.findById(productId);
        if (product.isPresent()) {
            Product updatedProduct = product.get();
            updatedProduct.setQuantityState(quantityState);
            productRepository.save(updatedProduct);
            return true;
        }

        return false;
    }

    public ProductDto getProductById(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundRuntimeException("Product not found with id: " + productId));
        return Mapper.toDto(product);
    }

    public List<ProductDto> findAllByIds(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }

        return productRepository.findAllById(ids).stream()
                .map(Mapper::toDto)
                .toList();
    }

}
