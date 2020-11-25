package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.jsons.PictureImportDto;
import softuni.exam.models.entities.Picture;
import softuni.exam.repository.CarRepository;
import softuni.exam.repository.PictureRepository;
import softuni.exam.service.PictureService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class PictureServiceImpl implements PictureService {
    private final static String PICTURE_PATH = "src/main/resources/files/json/pictures.json";
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;
    private final PictureRepository pictureRepository;
    private final CarRepository carRepository;
    @Autowired
    public PictureServiceImpl(ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson, PictureRepository pictureRepository, CarRepository carRepository) {
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
        this.pictureRepository = pictureRepository;
        this.carRepository = carRepository;

    }

    @Override
    public boolean areImported() {
        return this.pictureRepository.count() > 0;
    }

    @Override
    public String readPicturesFromFile() throws IOException {
        return String.join("", Files.readAllLines(Path.of(PICTURE_PATH)));
    }

    @Override
    public String importPictures() throws IOException {
        StringBuilder builder = new StringBuilder();
        PictureImportDto[] pictureImportDtos = this.gson.fromJson(this.readPicturesFromFile(), PictureImportDto[].class);
        for (PictureImportDto pictureImportDto : pictureImportDtos) {
            Optional<Picture> byName = this.pictureRepository.findByName(pictureImportDto.getName());
            if (validationUtil.isValid(pictureImportDto) && byName.isEmpty()) {
                Picture picture = this.modelMapper.map(pictureImportDto, Picture.class);
                picture.setCar(this.carRepository.findById(pictureImportDto.getCar()).get());
                this.pictureRepository.saveAndFlush(picture);
                builder.append(String.format("Successfully import picture - %s",pictureImportDto.getName()))
                        .append(System.lineSeparator());
            } else {
                builder.append("Invalid picture").append(System.lineSeparator());
            }
        }

        return builder.toString();
    }
}
