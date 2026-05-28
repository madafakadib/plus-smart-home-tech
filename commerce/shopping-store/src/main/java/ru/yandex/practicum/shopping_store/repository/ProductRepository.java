package ru.yandex.practicum.shopping_store.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.api.shoppingstore.ProductCategory;
import ru.yandex.practicum.api.shoppingstore.ProductState;
import ru.yandex.practicum.shopping_store.entity.Product;

public interface ProductRepository extends JpaRepository<Product, String> {
    Page<Product> findByProductCategoryAndProductState(ProductCategory category, ProductState state, Pageable pageable);
}
