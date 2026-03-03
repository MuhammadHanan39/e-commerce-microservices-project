package com.ecommerce.product.service.impl;

import com.ecommerce.product.dto.*;
import com.ecommerce.product.entity.Product;
import com.ecommerce.product.exception.DuplicateResourceException;
import com.ecommerce.product.exception.ResourceNotFoundException;
import com.ecommerce.product.mapper.ProductMapper;
import com.ecommerce.product.repository.ProductRepository;
import com.ecommerce.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        log.info("Creating new product: {}", request.getName());

        // Check if SKU already exists
        if (request.getSku() != null && productRepository.existsBySku(request.getSku())) {
            throw new DuplicateResourceException("Product with SKU " + request.getSku() + " already exists");
        }

        Product product = productMapper.toEntity(request);

        // Generate SKU if not provided
        if (product.getSku() == null || product.getSku().isEmpty()) {
            product.setSku(generateSku(product));
        }

        Product savedProduct = productRepository.save(product);
        log.info("Product created successfully with ID: {}", savedProduct.getId());

        return productMapper.toResponse(savedProduct);
    }

    @Override
    @Cacheable(value = "products", key = "#id")
    public ProductResponse getProductById(String id) {
        log.info("Fetching product with ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
        return productMapper.toResponse(product);
    }

    @Override
    @CachePut(value = "products", key = "#id")
    public ProductResponse updateProduct(String id, ProductUpdateRequest request) {
        log.info("Updating product with ID: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));

        // Check SKU uniqueness if updating
        if (request.getSku() != null && !request.getSku().equals(product.getSku())) {
            if (productRepository.existsBySku(request.getSku())) {
                throw new DuplicateResourceException("Product with SKU " + request.getSku() + " already exists");
            }
        }

        productMapper.updateEntityFromRequest(request, product);
        Product updatedProduct = productRepository.save(product);

        log.info("Product updated successfully: {}", id);
        return productMapper.toResponse(updatedProduct);
    }

    @Override
    @CacheEvict(value = "products", key = "#id")
    public void deleteProduct(String id) {
        log.info("Deleting product with ID: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));

        // Soft delete
        product.setActive(false);
        productRepository.save(product);

        log.info("Product deleted successfully: {}", id);
    }

    @Override
    public PageResponse<ProductResponse> getAllProducts(Pageable pageable) {
        log.info("Fetching all products - page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        Page<Product> productPage = productRepository.findByActiveTrue(pageable);
        return buildPageResponse(productPage);
    }

    @Override
    public PageResponse<ProductResponse> getProductsByCategory(String category, Pageable pageable) {
        log.info("Fetching products by category: {}", category);
        Page<Product> productPage = productRepository.findByCategoryAndActiveTrue(category, pageable);
        return buildPageResponse(productPage);
    }

    @Override
    public PageResponse<ProductResponse> searchProducts(String keyword, Pageable pageable) {
        log.info("Searching products with keyword: {}", keyword);
        Page<Product> productPage = productRepository.findByNameContainingIgnoreCaseAndActiveTrue(keyword, pageable);
        return buildPageResponse(productPage);
    }

    @Override
    public PageResponse<ProductResponse> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        log.info("Fetching products by price range: {} - {}", minPrice, maxPrice);
        Page<Product> productPage = productRepository.findByPriceRange(minPrice, maxPrice, pageable);
        return buildPageResponse(productPage);
    }

    @Override
    public PageResponse<ProductResponse> getProductsByBrand(String brand, Pageable pageable) {
        log.info("Fetching products by brand: {}", brand);
        Page<Product> productPage = productRepository.findByBrand(brand, pageable);
        return buildPageResponse(productPage);
    }

    @Override
    public List<String> getAllCategories() {
        log.info("Fetching all categories");
        return productRepository.findAllCategories();
    }

    @Override
    public List<String> getAllBrands() {
        log.info("Fetching all brands");
        return productRepository.findAllBrands();
    }

    @Override
    public List<ProductResponse> getLowStockProducts() {
        log.info("Fetching low stock products");
        List<Product> lowStockProducts = productRepository.findByQuantityLessThanEqual(10);
        return lowStockProducts.stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponse getProductBySku(String sku) {
        log.info("Fetching product by SKU: {}", sku);
        Product product = productRepository.findBySkuAndActiveTrue(sku)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with SKU: " + sku));
        return productMapper.toResponse(product);
    }

    @Override
    @Transactional
    public void updateStock(String productId, Integer quantity) {
        log.info("Updating stock for product: {} with quantity: {}", productId, quantity);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));

        product.setQuantity(product.getQuantity() + quantity);
        productRepository.save(product);

        log.info("Stock updated successfully for product: {}", productId);
    }

    private PageResponse<ProductResponse> buildPageResponse(Page<Product> productPage) {
        List<ProductResponse> products = productPage.getContent()
                .stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());

        return PageResponse.<ProductResponse>builder()
                .content(products)
                .pageNumber(productPage.getNumber())
                .pageSize(productPage.getSize())
                .totalElements(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .first(productPage.isFirst())
                .last(productPage.isLast())
                .build();
    }

    private String generateSku(Product product) {
        String prefix = product.getCategory() != null ?
                product.getCategory().substring(0, Math.min(3, product.getCategory().length())).toUpperCase() :
                "PRD";
        return prefix + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}