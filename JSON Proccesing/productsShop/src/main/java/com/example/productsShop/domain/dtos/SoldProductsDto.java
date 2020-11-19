package com.example.productsShop.domain.dtos;

import com.google.gson.annotations.Expose;

import java.math.BigDecimal;

public class SoldProductsDto {
    @Expose
    private String name;
    @Expose
    private BigDecimal price;


    public SoldProductsDto(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public SoldProductsDto() {
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
