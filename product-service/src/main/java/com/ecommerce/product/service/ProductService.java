package com.ecommerce.product.service;

import com.ecommerce.product.dto.*;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);
    ProductResponse getProductById(String id);
    ProductResponse updateProduct(String id, ProductUpdateRequest request);
    void deleteProduct(String id);
    PageResponse<ProductResponse> getAllProducts(Pageable pageable);
    PageResponse<ProductResponse> getProductsByCategory(String category, Pageable pageable);
    PageResponse<ProductResponse> searchProducts(String keyword, Pageable pageable);
    PageResponse<ProductResponse> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    PageResponse<ProductResponse> getProductsByBrand(String brand, Pageable pageable);
    List<String> getAllCategories();
    List<String> getAllBrands();
    List<ProductResponse> getLowStockProducts();
    ProductResponse getProductBySku(String sku);
    void updateStock(String productId, Integer quantity);
}