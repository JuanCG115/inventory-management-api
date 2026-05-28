package com.inventario.modulo.dto;

import jakarta.validation.constraints.NotNull;

public record StockAdjustmentDto(
        @NotNull(message = "The adjustment amount is mandatory")
        Integer quantity
) {
}
