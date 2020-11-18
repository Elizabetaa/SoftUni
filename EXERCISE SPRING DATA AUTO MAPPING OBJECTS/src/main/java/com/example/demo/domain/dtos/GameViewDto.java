package com.example.demo.domain.dtos;

import java.math.BigDecimal;

public class GameViewDto {
    private String title;
    private BigDecimal price;

    public GameViewDto(String title, BigDecimal price) {
        this.title = title;
        this.price = price;
    }

    public GameViewDto() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
