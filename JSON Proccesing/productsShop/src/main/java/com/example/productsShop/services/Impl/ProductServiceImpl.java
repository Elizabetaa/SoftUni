package com.example.productsShop.services.Impl;

import com.example.productsShop.domain.entities.Category;
import com.example.productsShop.domain.entities.Product;
import com.example.productsShop.domain.entities.User;
import com.example.productsShop.domain.dtos.ProductSeedDataDto;
import com.example.productsShop.repositories.CategoryRepository;
import com.example.productsShop.repositories.ProductRepository;
import com.example.productsShop.repositories.UserRepository;
import com.example.productsShop.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void seedData(ProductSeedDataDto[] productSeedDto) {

        Arrays.stream(productSeedDto).forEach(dto -> {
            User buyer = this.userRepository.findById(Long.parseLong(String.valueOf(getRandomUserId()))).orElse(null);
            User seller = this.userRepository.findById(Long.parseLong(String.valueOf(getRandomUserId()))).orElse(null);
            Category category = this.categoryRepository.findById(Long.parseLong(String.valueOf(getRandomCategory()))).get();
            Product product = new Product(dto.getName(),dto.getPrice(),buyer,seller,category);
            this.productRepository.saveAndFlush(product);
        });
    }

    @Override
    public List<Product> findByPriceBetweenAndBuyerIdIsNullOrderByPrice() {
        return this.productRepository.findByPriceBetweenAndBuyerIdIsNullOrderByPrice(
                new BigDecimal(500),new BigDecimal(1000)
        );
    }

    @Override
    public Set<Product> getProductsBySellerId(Long id) {
        return this.productRepository.getAllBySellerId(id);
    }

    private int getRandomCategory() {
        Random random = new Random();
        int bounds = random.nextInt((int) this.categoryRepository.count()) + 1;
        return bounds;
    }

    private int getRandomUserId() {
        Random random = new Random();
        int bounds = random.nextInt((int) this.userRepository.count());
        return bounds;
    }

}
