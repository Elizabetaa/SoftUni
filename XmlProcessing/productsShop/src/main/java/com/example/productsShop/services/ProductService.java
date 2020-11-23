package com.example.productsShop.services;

import javax.xml.bind.JAXBException;

public interface ProductService {

    void seedDataProducts() throws JAXBException;

    void findByPriceBetweenAndBuyerIdIsNullOrderByPrice() throws JAXBException;

    void getProductsBySellerId(Long id);
}
