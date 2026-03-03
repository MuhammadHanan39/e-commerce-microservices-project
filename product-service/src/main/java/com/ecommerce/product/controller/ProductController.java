package com.ecommerce.product.controller;

import com.ecommerce.product.dto.*;
import com.ecommerce.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Product Management", description = "APIs for managing products")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Create a new product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Product already exists")
    })
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        log.info("REST request to create product: {}", request.getName());
        ProductResponse response = productService.createProduct(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Get product by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(
            @Parameter(description = "Product ID") @PathVariable String id) {
        log.info("REST request to get product: {}", id);
        ProductResponse response = productService.getProductById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update an existing product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable String id,
            @Valid @RequestBody ProductUpdateRequest request) {
        log.info("REST request to update product: {}", id);
        ProductResponse response = productService.updateProduct(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        log.info("REST request to delete product: {}", id);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all products with pagination")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResponse<ProductResponse>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        log.info("REST request to get all products - page: {}, size: {}", page, size);

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        PageResponse<ProductResponse> response = productService.getAllProducts(pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get products by category")
    @GetMapping("/category/{category}")
    public ResponseEntity<PageResponse<ProductResponse>> getProductsByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("REST request to get products by category: {}", category);
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<ProductResponse> response = productService.getProductsByCategory(category, pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Search products by name")
    @GetMapping("/search")
    public ResponseEntity<PageResponse<ProductResponse>> searchProducts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("REST request to search products with keyword: {}", keyword);
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<ProductResponse> response = productService.searchProducts(keyword, pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get products by price range")
    @GetMapping("/price-range")
    public ResponseEntity<PageResponse<ProductResponse>> getProductsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("REST request to get products by price range: {} - {}", minPrice, maxPrice);
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<ProductResponse> response = productService.getProductsByPriceRange(minPrice, maxPrice, pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get products by brand")
    @GetMapping("/brand/{brand}")
    public ResponseEntity<PageResponse<ProductResponse>> getProductsByBrand(
            @PathVariable String brand,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("REST request to get products by brand: {}", brand);
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<ProductResponse> response = productService.getProductsByBrand(brand, pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all categories")
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        log.info("REST request to get all categories");
        List<String> categories = productService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "Get all brands")
    @GetMapping("/brands")
    public ResponseEntity<List<String>> getAllBrands() {
        log.info("REST request to get all brands");
        List<String> brands = productService.getAllBrands();
        return ResponseEntity.ok(brands);
    }

    @Operation(summary = "Get low stock products")
    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductResponse>> getLowStockProducts() {
        log.info("REST request to get low stock products");
        List<ProductResponse> products = productService.getLowStockProducts();
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Get product by SKU")
    @GetMapping("/sku/{sku}")
    public ResponseEntity<ProductResponse> getProductBySku(@PathVariable String sku) {
        log.info("REST request to get product by SKU: {}", sku);
        ProductResponse response = productService.getProductBySku(sku);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update product stock")
    @PatchMapping("/{id}/stock")
    public ResponseEntity<Void> updateStock(
            @PathVariable String id,
            @RequestParam Integer quantity) {
        log.info("REST request to update stock for product: {} with quantity: {}", id, quantity);
        productService.updateStock(id, quantity);
        return ResponseEntity.ok().build();
    }
}