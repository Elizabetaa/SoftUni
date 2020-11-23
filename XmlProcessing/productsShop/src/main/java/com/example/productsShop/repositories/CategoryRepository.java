package com.example.productsShop.repositories;

import com.example.productsShop.domain.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c "+
            "GROUP BY c.id ORDER BY c.products.size")
    List<Category> getAllCategoriesByProductsCount();
}
