package com.example.productsShop;

import com.example.productsShop.services.CategoryService;
import com.example.productsShop.services.ProductService;
import com.example.productsShop.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

@Controller
public class AppController implements CommandLineRunner {

    private final CategoryService categoryService;
    private final UserService userService;
    private final ProductService productService;

    @Autowired
    public AppController(CategoryService categoryService, UserService userService, ProductService productService) {
        this.categoryService = categoryService;
        this.userService = userService;
        this.productService = productService;
    }


    @Override
    public void run(String... args) throws Exception {
//        this.userService.seedUsersData();
//        this.productService.seedDataProducts();
//        this.categoryService.seedCategoriesData();
//
//        //Query 1
//        this.productService.findByPriceBetweenAndBuyerIdIsNullOrderByPrice();
//        //Query2
//        this.userService.getAllUsersWithSales();
//        //Query3
//        this.categoryService.getAllCategoriesByProductsCount();

        this.userService.getAllUsersWithSalesOrdered();
    }


}
