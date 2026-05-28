package com.inventario.modulo.service;


import com.inventario.modulo.Product;
import com.inventario.modulo.dto.ProductRequestDto;
import com.inventario.modulo.dto.ProductResponseDto;
import com.inventario.modulo.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAll().
                stream().
                map(ProductResponseDto::new).
                collect(Collectors.toList());
    }

    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto request) {
        if (productRepository.findByUniqueCode(request.uniqueCode()).isPresent()) {
            throw new IllegalArgumentException("A product with that unique code already exists: " + request.uniqueCode());
        }

        Product product = new Product();
        product.setUniqueCode(request.uniqueCode());
        product.setName(request.name());
        product.setDescription(request.description());
        product.setActualStock(request.actualStock());
        product.setMinimumStock(request.minimumStock());
        product.setPrice(request.price());

        Product savedProduct = productRepository.save(product);
        return new ProductResponseDto(savedProduct);
    }

    @Transactional
    public ProductResponseDto adjustStock(String uniqueCode, int quantity) {
        Product product = productRepository.findByUniqueCode(uniqueCode)
                .orElseThrow(() -> new IllegalArgumentException("The product with the code was not found: " + uniqueCode));

        if (product.getActualStock() + quantity < 0) {
            throw new IllegalArgumentException("Operation rejected: insufficient stock. actual stock: " + product.getActualStock() + ", withdrawal attempt: " + Math.abs(quantity));
        }

        product.setActualStock(product.getActualStock() + quantity);

        Product updateProduct = productRepository.save(product);
        return new ProductResponseDto(updateProduct);
    }
}
