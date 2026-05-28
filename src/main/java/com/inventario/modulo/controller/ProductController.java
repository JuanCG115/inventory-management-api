package com.inventario.modulo.controller;


import com.inventario.modulo.dto.ProductRequestDto;
import com.inventario.modulo.dto.ProductResponseDto;
import com.inventario.modulo.dto.StockAdjustmentDto;
import com.inventario.modulo.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        List<ProductResponseDto> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody ProductRequestDto request) {
        ProductResponseDto createProduct = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createProduct);
    }

    @PatchMapping("/{uniqueCode}/stock")
    public ResponseEntity<ProductResponseDto> adjustStock(
            @PathVariable String uniqueCode,
            @Valid @RequestBody StockAdjustmentDto stockAdjustmentDto) {

        ProductResponseDto updateProduct = productService.adjustStock(uniqueCode, stockAdjustmentDto.quantity());
        return ResponseEntity.ok(updateProduct);
    }
}
