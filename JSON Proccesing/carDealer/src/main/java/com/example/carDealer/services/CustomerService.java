package com.example.carDealer.services;

import java.io.IOException;

public interface CustomerService {
    void seedCustomers() throws IOException;

    String orderedCustomers();

    String getAllWithBoughtCars();
}
