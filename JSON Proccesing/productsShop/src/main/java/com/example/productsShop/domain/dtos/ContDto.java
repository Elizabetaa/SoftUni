package com.example.productsShop.domain.dtos;

import com.google.gson.annotations.Expose;

import java.util.List;

public class ContDto {
    @Expose
    private int count;
    @Expose
    private List<SoldProductsDto> products;

    public ContDto(int count, List<SoldProductsDto> products) {
        this.count = count;
        this.products = products;
    }
}
