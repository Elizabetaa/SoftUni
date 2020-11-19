package com.example.productsShop.controllers;

import com.example.productsShop.constants.GlobalConstants;
import com.example.productsShop.domain.dtos.*;
import com.example.productsShop.domain.entities.Product;
import com.example.productsShop.domain.entities.User;
import com.example.productsShop.services.CategoryService;
import com.example.productsShop.services.ProductService;
import com.example.productsShop.services.UserService;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Controller
public class AppController implements CommandLineRunner {
    private final Gson gson;
    private final CategoryService categoryService;
    private final UserService userService;
    private final ProductService productService;
    private final ModelMapper modelMapper;

    @Autowired
    public AppController(Gson gson, CategoryService categoryService, UserService userService, ProductService productService, ModelMapper modelMapper) {
        this.gson = gson;
        this.categoryService = categoryService;
        this.userService = userService;
        this.productService = productService;

        this.modelMapper = modelMapper;
    }

    @Override
    public void run(String... args) throws Exception {

        this.seedUsers();
        this.seedCategories();
        this.seedProducts();

        //Query1
        ProductsInRange();

        //Query2
        successfullySoldProducts();

        //Query3
        System.out.println(this.categoryService.getAllCategoriesByProductsCount());

        //Query4
        UsersAndProducts();


    }

    private void ProductsInRange() {
        List<Product> pr = this.productService.findByPriceBetweenAndBuyerIdIsNullOrderByPrice();

        for (Product product : pr) {
            String fullName = product.getSeller().getFirstName()+" " +product.getSeller().getLastName();
            ProductInRangeDto productInRangeDto = new ProductInRangeDto(product.getName(),product.getPrice(),fullName);

            System.out.println(gson.toJson(productInRangeDto));
        }
    }

    private void successfullySoldProducts() {
        List<User> users = this.userService.getAllUsersWithSales();
        for (User user : users) {
            Set<ProductDto> products = new LinkedHashSet<>();
            for (Product sellingProduct : user.getSellingProducts()) {
                if (sellingProduct.getBuyer() == null) {
                    break;
                }
                products.add(new ProductDto(sellingProduct.getName(), sellingProduct.getPrice(),
                        sellingProduct.getBuyer().getFirstName(),
                        sellingProduct.getBuyer().getLastName()
                ));
            }

            UsersWithSoldProductsDto usersWithSoldProductsDto =
                    new UsersWithSoldProductsDto(
                            user.getFirstName(), user.getLastName(),
                            products
                    );

            System.out.println(gson.toJson(usersWithSoldProductsDto));
        }
    }

    private void UsersAndProducts() {
        List<User> userList = this.userService.getAllUsersWithSalesOrdered();
        for (User user : userList) {
            List<SoldProductsDto> products = new ArrayList<>();
            for (Product sellingProduct : user.getSellingProducts()) {
                if (sellingProduct.getBuyer() == null){
                    break;
                }
                products.add(new SoldProductsDto(sellingProduct.getName(),sellingProduct.getPrice()));

            }
            ContDto contDto = new ContDto(products.size(),products);
            UsersProductsDto usersProductsDto = new UsersProductsDto(user.getFirstName(),user.getLastName(),
                    user.getAge(),contDto);
            System.out.println(gson.toJson(usersProductsDto));
        }
    }

    private void seedProducts() throws FileNotFoundException {

        ProductSeedDataDto[] productSeedDto = this.gson
                .fromJson(new FileReader(GlobalConstants.PRODUCTS_FILE_PATH),
                        ProductSeedDataDto[].class);


        this.productService.seedData(productSeedDto);
    }


    private void seedUsers() throws FileNotFoundException {
        UserSeedDto[] dtos = this.gson
                .fromJson(new FileReader(GlobalConstants.USERS_FILE_PATH),
                        UserSeedDto[].class);
        this.userService.seedData(dtos);
    }

    private void seedCategories() throws FileNotFoundException {
        CategorySeedDto[] dtos = this.gson
                .fromJson(new FileReader(GlobalConstants.CATEGORIES_FILE_PATH),
                        CategorySeedDto[].class);

        this.categoryService.seedData(dtos);
    }

}
