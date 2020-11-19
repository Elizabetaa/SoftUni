package com.example.productsShop.services.Impl;

import com.example.productsShop.domain.dtos.CategoryByProductsCountDto;
import com.example.productsShop.domain.entities.Category;
import com.example.productsShop.domain.dtos.CategorySeedDto;
import com.example.productsShop.domain.entities.Product;
import com.example.productsShop.repositories.CategoryRepository;
import com.example.productsShop.repositories.ProductRepository;
import com.example.productsShop.services.CategoryService;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final Gson gson;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ProductRepository productRepository, Gson gson) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.gson = gson;
    }

    @Override
    public void seedData(CategorySeedDto[] dtos) {
        if (this.categoryRepository.count() != 0) {
            return;
        }
        Arrays.stream(dtos).forEach(c -> {
            Category category = new Category(c.getName());
            this.categoryRepository.saveAndFlush(category);
        });

    }

    @Override
    public String getAllCategoriesByProductsCount() {
        List<Category> categories = this.categoryRepository.getAllCategoriesByProductsCount();

        List<CategoryByProductsCountDto> dtos = new ArrayList<>();

        for (Category category : categories) {
            CategoryByProductsCountDto map = new CategoryByProductsCountDto();
            map.setCategory(category.getName());
            map.setProductCount(category.getProducts().size());

            double price = 0;
            for (Product product : category.getProducts()) {
                price += Double.parseDouble(String.valueOf(product.getPrice()));
            }

            double avg = price / category.getProducts().size();
            map.setAveragePrice(BigDecimal.valueOf(avg).
                    setScale(2, RoundingMode.HALF_UP));

            map.setTotalRevenue(BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP));

            dtos.add(map);
        }
        return this.gson.toJson(dtos);
    }


}
