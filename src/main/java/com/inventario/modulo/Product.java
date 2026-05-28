package com.inventario.modulo;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "The unique code is mandatory")
    @Size(max = 50, message = "The unique code cannot be longer than 50 characters")
    @Column(name = "unique_code", nullable = false, unique = true, length = 50)
    private String uniqueCode;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "The name cannot be longer than 100 characters")
    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Min(value = 0, message = "The actual stock cannot be negative")
    @Column(name = "actual_stock", nullable = false)
    private int actualStock;

    @Min(value = 0, message = "The minimum stock cannot be negative")
    @Column(name = "minimum_stock", nullable = false)
    private int minimumStock;

    @NotNull(message = "The price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "The price cannot be lower than zero")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDateTime creationDate;

    @Column(name = "update_date", nullable = false)
    private LocalDateTime updateDate;

    public Product() {
    }

    public Product(String uniqueCode, String name, String description,
                   int actualStock, int minimumStock, BigDecimal price) {
        this.uniqueCode = uniqueCode;
        this.name = name;
        this.description = description;
        this.actualStock = actualStock;
        this.minimumStock = minimumStock;
        this.price = price;
    }

    @PrePersist
    protected void onCreate() {
        this.creationDate = LocalDateTime.now();
        this.updateDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateDate = LocalDateTime.now();
    }

    public boolean requireSupply() {
        return this.actualStock <= this.minimumStock;
    }

    public void adjustStock(int amount) {
        if (this.actualStock + amount < 0) {
            throw new IllegalArgumentException("The resulting stock cannot be negative");
        }
        this.actualStock += amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getActualStock() {
        return actualStock;
    }

    public void setActualStock(int actualStock) {
        this.actualStock = actualStock;
    }

    public int getMinimumStock() {
        return minimumStock;
    }

    public void setMinimumStock(int minimumStock) {
        this.minimumStock = minimumStock;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }
}
