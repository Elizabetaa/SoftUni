package com.example.carDealer.services;

import java.io.IOException;

public interface SupplierService {

    void seedSupplier() throws IOException;

    String getAllSuppliers();
}
