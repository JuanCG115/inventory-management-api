package com.inventario.modulo.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductRequestDto(
        @NotBlank(message = "The unique code is required")
        @Size(max = 50, message = "The unique code cannot be longer than 50 characters")
        String uniqueCode,

        @NotBlank(message = "The name is required")
        @Size(max = 100, message = "The name cannot be longer than 100 characters")
        String name,

        String description,

        @Min(value = 0, message = "The initial stock cannot be negative")
        int actualStock,

        @Min(value = 0, message = "The minimum stock cannot be negative")
        int minimumStock,

        @NotNull(message = "The price is required")
        @DecimalMin(value = "0.0", inclusive = true, message = "The price cannot be lower than zero")
        BigDecimal price
) {
}
