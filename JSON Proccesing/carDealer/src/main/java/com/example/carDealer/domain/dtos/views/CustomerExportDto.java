package com.example.carDealer.domain.dtos.views;

import com.example.carDealer.domain.entities.Sale;
import com.google.gson.annotations.Expose;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

public class CustomerExportDto {
    @Expose
    private Long id;
    @Expose
    private String name;
    @Expose
    private Date birthDate;
    @Expose
    private boolean isYoungDriver;
    @Expose
    private Set<SalesDto> sales;


    public CustomerExportDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public boolean isYoungDriver() {
        return isYoungDriver;
    }

    public void setYoungDriver(boolean youngDriver) {
        isYoungDriver = youngDriver;
    }

    public Set<SalesDto> getSales() {
        return sales;
    }

    public void setSales(Set<SalesDto> sales) {
        this.sales = sales;
    }
}
