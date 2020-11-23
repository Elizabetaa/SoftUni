package com.example.productsShop.services;

import javax.xml.bind.JAXBException;

public interface UserService {
    void seedUsersData() throws JAXBException;

    void getAllUsersWithSales() throws JAXBException;

    void getAllUsersWithSalesOrdered() throws JAXBException;
}
