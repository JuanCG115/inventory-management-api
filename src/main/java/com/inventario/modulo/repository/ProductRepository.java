package com.inventario.modulo.repository;

import com.inventario.modulo.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByUniqueCode(String uniqueCode);
    List<Product> findByActualStockLessThanEqual(int minimumStock);

    @Query("SELECT p FROM Product p WHERE p.actualStock <= p.minimumStock")
    List<Product> findProductsLowOfStock();

    @Query(value = "SELECT EXISTS(SELECT 1 FROM products WHERE unique_code = :code)", nativeQuery = true)
    boolean existsByUniqueCode(@Param("code") String code);
}
