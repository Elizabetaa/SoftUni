package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.jsons.CarImportDto;
import softuni.exam.models.entities.Car;
import softuni.exam.repository.CarRepository;
import softuni.exam.service.CarService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

@Service
public class CarServiceImpl implements CarService {
    private final static String CAR_PATH = "src/main/resources/files/json/cars.json";
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;
    private final CarRepository carRepository;

    @Autowired
    public CarServiceImpl(ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson, CarRepository carRepository) {
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
        this.carRepository = carRepository;
    }

    @Override
    public boolean areImported() {
        return this.carRepository.count() > 0;
    }

    @Override
    public String readCarsFileContent() throws IOException {
        List<String> strings = Files.readAllLines(Path.of(CAR_PATH));

        return String.join("", strings);
    }

    @Override
    public String importCars() throws IOException {
        StringBuilder builder = new StringBuilder();
        CarImportDto[] carImportDtos = this.gson.fromJson(this.readCarsFileContent(), CarImportDto[].class);
        for (CarImportDto carImportDto : carImportDtos) {
            if (validationUtil.isValid(carImportDto)) {
                this.carRepository.saveAndFlush(this.modelMapper.map(carImportDto, Car.class));
                builder.append(String.format("Successfully imported car - %s - %s",
                        carImportDto.getMake(),
                        carImportDto.getModel()))
                        .append(System.lineSeparator());
            } else {
                builder.append("Invalid car").append(System.lineSeparator());
            }
        }

        return builder.toString();
    }

    @Override
    public String getCarsOrderByPicturesCountThenByMake() {
        StringBuilder builder = new StringBuilder();
        Set<Car> cars = this.carRepository.findByPicturesSizeAndMake();
        for (Car car : cars) {
            builder.append(String.format("Car make - %s, model - %s%n" +
                    "\tKilometers - %d%n" +
                    "\tRegistered on - %S%n" +
                    "\tNumber of pictures - %d%n",car.getMake(),car.getModel(),
                    car.getKilometers(),car.getRegisteredOn(),car.getPictures().size()));
        }

        return builder.toString();
    }
}
