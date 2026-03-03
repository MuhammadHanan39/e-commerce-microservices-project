package com.ecommerce.product.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductUpdateRequest {

    @Size(min = 3, max = 200, message = "Product name must be between 3 and 200 characters")
    private String name;

    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;

    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @DecimalMax(value = "999999.99", message = "Price cannot exceed 999999.99")
    private BigDecimal price;

    private String category;
    private String sku;

    @Pattern(regexp = "^(http|https)://.*$", message = "Invalid URL format")
    private String imageUrl;

    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;

    @Min(value = 0, message = "Min stock level cannot be negative")
    private Integer minStockLevel;

    private Boolean active;
    private String brand;

    @Min(value = 0, message = "Weight cannot be negative")
    private Double weight;

    private String dimensions;
    private Set<String> tags;
}