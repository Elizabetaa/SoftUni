package com.example.carDealer.domain.dtos.views;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SalesDto {
    @Expose
    @SerializedName("car")
    private String carMake;

    public SalesDto() {

    }

    public String getCarMake() {
        return carMake;
    }

    public void setCarMake(String carMake) {
        this.carMake = carMake;
    }
}
