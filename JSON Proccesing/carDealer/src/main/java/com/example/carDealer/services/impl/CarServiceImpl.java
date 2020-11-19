package com.example.carDealer.services.impl;

import com.example.carDealer.domain.dtos.seedDataDtos.CarSeedDataDto;
import com.example.carDealer.domain.dtos.views.CarFromMakeToyotaDto;
import com.example.carDealer.domain.dtos.views.CarWithPartsDto;
import com.example.carDealer.domain.entities.Car;
import com.example.carDealer.domain.entities.Part;
import com.example.carDealer.domain.repositories.CarRepository;
import com.example.carDealer.domain.repositories.PartRepository;
import com.example.carDealer.services.CarService;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Service
public class CarServiceImpl implements CarService {
    private final static String CAR_PATH = "src/main/resources/jsons/cars.json";
    private final CarRepository carRepository;
    private final PartRepository partRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;

    @Autowired
    public CarServiceImpl(CarRepository carRepository, PartRepository partRepository,
                          ModelMapper modelMapper, Gson gson) {
        this.carRepository = carRepository;
        this.partRepository = partRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
    }

    @Override
    @Transactional
    public void seedCar() throws IOException {
        String content = String.join("",
                Files.readAllLines(Paths.get(CAR_PATH)));

        CarSeedDataDto[] carSeedDataDtos = this.gson.fromJson(content,CarSeedDataDto[].class);
        for (CarSeedDataDto carSeedDataDto : carSeedDataDtos) {
            Car car = this.modelMapper.map(carSeedDataDto,Car.class);
            car.setParts(getTenRandomParts());
            this.carRepository.saveAndFlush(car);
        }
    }

    @Override
    public String getCarByMake() {
        Set<Car> cars = this.carRepository.findAllByMakeOrderByModelAscTravelledDistanceDesc("Toyota");
        List<CarFromMakeToyotaDto> toExport = new ArrayList<>();
        for (Car car : cars) {
            CarFromMakeToyotaDto carFromToyota = this.modelMapper.map(car,CarFromMakeToyotaDto.class);
            toExport.add(carFromToyota);
        }
        return this.gson.toJson(toExport);
    }

    @Override
    public String getCarsWithParts() {
        List<Car> cars = this.carRepository.findAll();
        List<CarWithPartsDto> carWithPartsDtos = new ArrayList<>();

        for (Car car : cars) {
            CarWithPartsDto carPart = this.modelMapper.map(car,CarWithPartsDto.class);
            carWithPartsDtos.add(carPart);
        }

        return this.gson.toJson(carWithPartsDtos);
    }

    private Set<Part> getTenRandomParts() {
        Random random = new Random();
        Set<Part> parts = new LinkedHashSet<>();
        for (int i = 0; i < 5; i++) {
            long index = (long) random.nextInt((int) this.partRepository.count()) + 1;
            parts.add(this.partRepository.findById(index).get());
        }

        return parts;
    }
}
