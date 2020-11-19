package com.example.carDealer.domain.dtos.views;

import com.example.carDealer.domain.entities.Part;
import com.google.gson.annotations.Expose;

import java.util.List;
import java.util.Set;

public class CarWithPartsDto {

    @Expose
    private CarDto car;
    @Expose
    private List<PartDto> parts;

    public CarWithPartsDto() {
    }

    public CarDto getCar() {
        return car;
    }

    public void setCar(CarDto car) {
        this.car = car;
    }

    public List<PartDto> getParts() {
        return parts;
    }

    public void setParts(List<PartDto> parts) {
        this.parts = parts;
    }
}
