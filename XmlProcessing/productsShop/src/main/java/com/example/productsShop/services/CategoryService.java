package com.example.productsShop.services;

import javax.xml.bind.JAXBException;

public interface CategoryService {

    void seedCategoriesData() throws JAXBException;

    void getAllCategoriesByProductsCount() throws JAXBException;
}
