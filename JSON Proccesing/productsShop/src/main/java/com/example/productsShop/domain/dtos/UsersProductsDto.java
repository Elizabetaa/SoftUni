package com.example.productsShop.domain.dtos;

import com.google.gson.annotations.Expose;

public class UsersProductsDto {
    @Expose
    private String firstName;
    @Expose
    private String lastName;
    @Expose
    private int age;
    @Expose
    private ContDto soldProducts;

    public UsersProductsDto(String firstName, String lastName, int age, ContDto soldProducts) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.soldProducts = soldProducts;
    }


    public UsersProductsDto() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public ContDto getSoldProducts() {
        return soldProducts;
    }

    public void setSoldProducts(ContDto soldProducts) {
        this.soldProducts = soldProducts;
    }
}
