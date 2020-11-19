package com.example.productsShop.domain.dtos;

import com.example.productsShop.domain.entities.Category;
import com.example.productsShop.domain.entities.User;
import com.google.gson.annotations.Expose;

import java.math.BigDecimal;
import java.util.Set;

public class ProductSeedDataDto {
    @Expose
    private String name;
    @Expose
    private BigDecimal price;
    @Expose
    private User buyerId;
    @Expose
    private User sellerId;
    @Expose
    private Set<Category> categories;

    public ProductSeedDataDto() {
    }

    public ProductSeedDataDto(String name, BigDecimal price, User buyerId, User sellerId, Set<Category> categories) {
        this.name = name;
        this.price = price;
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.categories = categories;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public User getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(User buyerId) {
        this.buyerId = buyerId;
    }

    public User getSellerId() {
        return sellerId;
    }

    public void setSellerId(User sellerId) {
        this.sellerId = sellerId;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }
}
