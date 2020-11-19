package com.example.productsShop.services;

import com.example.productsShop.domain.dtos.CategorySeedDto;
import com.example.productsShop.domain.entities.Category;

import java.util.List;

public interface CategoryService  {

    void seedData(CategorySeedDto[] dtos);

   String getAllCategoriesByProductsCount();
}
