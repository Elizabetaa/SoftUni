package com.example.productsShop.services.Impl;

import com.example.productsShop.domain.entities.Product;
import com.example.productsShop.domain.entities.User;
import com.example.productsShop.domain.dtos.UserSeedDto;
import com.example.productsShop.repositories.UserRepository;
import com.example.productsShop.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(ModelMapper modelMapper, UserRepository userRepository) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    @Override
    public void seedData(UserSeedDto[] dtos) {
        if (this.userRepository.count() != 0) {
            return;
        }


        Arrays.stream(dtos).forEach(u -> {
            System.out.println(u);
            User user = this.modelMapper.map(u,User.class);

            this.userRepository.saveAndFlush(user);

        });
    }

    @Override
    public List<User> getAllUsersWithSales() {
        List<User> users = new ArrayList<>();
                this.userRepository.getAllUsersWithSales().forEach(u->{
                    for (Product sellingProduct : u.getSellingProducts()) {
                        if (sellingProduct.getBuyer() != null){
                            users.add(u);
                        }
                    }
        });
        return users;
    }

    @Override
    public List<User> getAllUsersWithSalesOrdered() {
        return this.userRepository.getAllUsersWithSalesOrdered();
    }
}
