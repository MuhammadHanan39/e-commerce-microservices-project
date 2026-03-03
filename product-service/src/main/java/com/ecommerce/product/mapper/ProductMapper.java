package com.ecommerce.product.mapper;

import com.ecommerce.product.dto.ProductRequest;
import com.ecommerce.product.dto.ProductResponse;
import com.ecommerce.product.dto.ProductUpdateRequest;
import com.ecommerce.product.entity.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {

    Product toEntity(ProductRequest productRequest);

    @Mapping(target = "stockStatus", expression = "java(calculateStockStatus(product))")
    ProductResponse toResponse(Product product);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(ProductUpdateRequest request, @MappingTarget Product product);

    default String calculateStockStatus(Product product) {
        if (product.getQuantity() <= 0) {
            return "OUT_OF_STOCK";
        } else if (product.getQuantity() <= product.getMinStockLevel()) {
            return "LOW_STOCK";
        } else {
            return "IN_STOCK";
        }
    }





}
