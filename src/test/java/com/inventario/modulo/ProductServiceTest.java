package com.inventario.modulo;

import com.inventario.modulo.dto.ProductRequestDto;
import com.inventario.modulo.dto.ProductResponseDto;
import com.inventario.modulo.repository.ProductRepository;
import com.inventario.modulo.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("It will correctly adjust the stock by reducing the available quantity.")
    void shouldAdjustStockCorrectly() {
        String code = "PROD-MOUSE";
        Product mockProduct = new Product();
        mockProduct.setUniqueCode(code);
        mockProduct.setName("Mouse Mecanico");
        mockProduct.setActualStock(15);
        mockProduct.setMinimumStock(5);
        mockProduct.setPrice(new BigDecimal("29.99"));

        when(productRepository.findByUniqueCode(code)).thenReturn(Optional.of(mockProduct));
        when(productRepository.save(any(Product.class))).thenReturn(mockProduct);

        ProductResponseDto response = productService.adjustStock(code, -12);

        assertNotNull(response);
        assertEquals(3, response.actualStock(), "The final stock should be 3");
        assertTrue(response.requireSupply(), "It should require replacement because 3 is less than or equal to 5.");

        verify(productRepository, times(1)).findByUniqueCode(code);
        verify(productRepository, times(1)).save(mockProduct);
    }

    @Test
    @DisplayName("It should throw an IllegalArgumentException if the resulting stock is negative.")
    void shouldThrowExceptionWhenStockIsInsufficient() {
        String code = "PROD-MOUSE";
        Product mockProduct = new Product();
        mockProduct.setUniqueCode(code);
        mockProduct.setActualStock(5);

        when(productRepository.findByUniqueCode(code)).thenReturn(Optional.of(mockProduct));


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.adjustStock(code, -10);
        });

        assertTrue(exception.getMessage().contains("Operation rejected: insufficient stock"));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("It should throw an IllegalArgumentException if you try to create a product with duplicate code.")
    void shouldThrowExceptionWhenCreatingDuplicateProduct() {
        ProductRequestDto requestDto = new ProductRequestDto(
                "PROD-DUP", "Product duplicated", "Description", 10, 2, new BigDecimal("10.00")
        );

        when(productRepository.findByUniqueCode("PROD-DUP")).thenReturn(Optional.of(new Product()));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.createProduct(requestDto);
        });

        assertTrue(exception.getMessage().contains("A product with that unique code already exists"));
        verify(productRepository, never()).save(any(Product.class));
    }
}