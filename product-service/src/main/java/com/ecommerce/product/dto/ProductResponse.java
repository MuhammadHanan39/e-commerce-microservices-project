package com.ecommerce.product.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private String sku;
    private String imageUrl;
    private Integer quantity;
    private Integer minStockLevel;
    private Boolean active;
    private String brand;
    private Double weight;
    private String dimensions;
    private Set<String> tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String stockStatus; // IN_STOCK, LOW_STOCK, OUT_OF_STOCK
}