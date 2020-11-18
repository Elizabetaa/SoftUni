package com.example.demo.services;

import com.example.demo.domain.dtos.UserLoginDto;
import com.example.demo.domain.dtos.UserRegisterDto;

public interface UserService {
    void registerService(UserRegisterDto userRegisterDto);
    void loginUser(UserLoginDto userLoginDto);
    void logout();

    boolean isLoggedUserIsAdmin();

    String getLoggedInUserByEmail();
}
