package com.example.carDealer.services;

import java.io.IOException;

public interface CarService {
    void seedCar() throws IOException;

    String getCarByMake();

    String getCarsWithParts();
}
