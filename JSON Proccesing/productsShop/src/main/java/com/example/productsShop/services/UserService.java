package com.example.productsShop.services;

import com.example.productsShop.domain.entities.User;
import com.example.productsShop.domain.dtos.UserSeedDto;

import java.util.List;

public interface UserService {
    void seedData(UserSeedDto[] dtos);

    List<User> getAllUsersWithSales();

    List<User> getAllUsersWithSalesOrdered();
}
