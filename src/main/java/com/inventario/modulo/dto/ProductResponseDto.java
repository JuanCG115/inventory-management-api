package com.inventario.modulo.dto;

import com.inventario.modulo.Product;

import java.math.BigDecimal;

public record ProductResponseDto(
        Long id,
        String uniqueCode,
        String name,
        String description,
        int actualStock,
        int minimumStock,
        BigDecimal price,
        Boolean requireSupply
) {

    public ProductResponseDto(Product product) {
        this(
                product.getId(),
                product.getUniqueCode(),
                product.getName(),
                product.getDescription(),
                product.getActualStock(),
                product.getMinimumStock(),
                product.getPrice(),
                product.requireSupply()
        );
    }
}
