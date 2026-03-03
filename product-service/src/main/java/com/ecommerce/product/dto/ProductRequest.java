package com.ecommerce.product.dto;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {
    @NotBlank(message = "Product name is required")
    @Size(min = 3, max = 200, message = "Product name must be between 3 and 200 characters")
    private String name;

    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @DecimalMax(value = "999999.99", message = "Price cannot exceed 999999.99")
    private BigDecimal price;

    @NotBlank(message = "Category is required")
    private String category;

    private String sku;

    @Pattern(regexp = "^(http|https)://.*$", message = "Invalid URL format")
    private String imageUrl;

    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity = 0;

    @Min(value = 0, message = "Min stock level cannot be negative")
    private Integer minStockLevel = 10;

    private Boolean active = true;

    private String brand;

    @Min(value = 0, message = "Weight cannot be negative")
    private Double weight;

    private String dimensions;

    private Set<String> tags;


}
