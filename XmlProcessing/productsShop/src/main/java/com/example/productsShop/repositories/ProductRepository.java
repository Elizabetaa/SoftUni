package com.example.productsShop.repositories;

import com.example.productsShop.domain.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

@Query("SELECT p FROM Product p " +
        "WHERE p.price BETWEEN 500 AND 1000 AND p.buyer.id is not null " +
        "ORDER BY p.price")
    List<Product> findByPriceBetweenAndBuyerIdIsNullOrderByPrice();

    @Query("SELECT p FROM Product p WHERE p.seller.id = :id")
    Set<Product> getAllBySellerId(Long id);

    List<Product> findAllByName(String name);
}
