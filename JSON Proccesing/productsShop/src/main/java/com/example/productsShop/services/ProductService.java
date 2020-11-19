package com.example.productsShop.services;

import com.example.productsShop.domain.entities.Product;
import com.example.productsShop.domain.dtos.ProductSeedDataDto;

import java.util.List;
import java.util.Set;

public interface ProductService {
    void seedData(ProductSeedDataDto[] productSeedDto);
    //List<Product> findByPriceBetweenAndBuyerIdIsNullOrderByPrice();

    List<Product> findByPriceBetweenAndBuyerIdIsNullOrderByPrice();

    Set<Product> getProductsBySellerId(Long id);
}
