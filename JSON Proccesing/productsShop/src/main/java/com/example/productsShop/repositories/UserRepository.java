package com.example.productsShop.repositories;

import com.example.productsShop.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT DISTINCT u FROM User u INNER JOIN Product p ON u.id = p.seller.id " +
            "WHERE p.buyer.id IS NOT NULL AND u.sellingProducts.size > 0")
    List<User> getAllUsersWithSales();

    @Query("SELECT DISTINCT u FROM User u INNER JOIN Product p ON u.id = p.seller.id " +
            "GROUP BY u.id " +
            "ORDER BY p.seller.sellingProducts.size DESC , u.lastName ASC ")
    List<User> getAllUsersWithSalesOrdered();
}
