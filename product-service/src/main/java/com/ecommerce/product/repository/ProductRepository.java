package com.ecommerce.product.repository;

import com.ecommerce.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    Page<Product> findByActiveTrue(Pageable pageable);

    Page<Product> findByCategoryAndActiveTrue(String category, Pageable pageable);

    Page<Product> findByNameContainingIgnoreCaseAndActiveTrue(String name, Pageable pageable);

    Optional<Product> findBySkuAndActiveTrue(String sku);

    List<Product> findByQuantityLessThanEqual(Integer quantity);

    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice AND p.active = true")
    Page<Product> findByPriceRange(@Param("minPrice") BigDecimal minPrice,
                                   @Param("maxPrice") BigDecimal maxPrice,
                                   Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.brand = :brand AND p.active = true")
    Page<Product> findByBrand(@Param("brand") String brand, Pageable pageable);

    @Query("SELECT DISTINCT p.category FROM Product p WHERE p.active = true")
    List<String> findAllCategories();

    @Query("SELECT DISTINCT p.brand FROM Product p WHERE p.active = true")
    List<String> findAllBrands();

    @Query("SELECT p FROM Product p JOIN p.tags t WHERE t IN :tags AND p.active = true")
    Page<Product> findByTags(@Param("tags") List<String> tags, Pageable pageable);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.category = :category AND p.active = true")
    Long countByCategory(@Param("category") String category);

    boolean existsBySku(String sku);
}