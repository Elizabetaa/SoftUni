package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.jsons.TownImportDto;
import softuni.exam.models.entities.Town;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class TownServiceImpl implements TownService {
    private final static String TOWN_PATH = "src/main/resources/files/json/towns.json";
    private final TownRepository townRepository;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    @Autowired
    public TownServiceImpl(TownRepository townRepository, Gson gson, ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.townRepository = townRepository;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean areImported() {
        return this.townRepository.count() > 0;
    }

    @Override
    public String readTownsFileContent() throws IOException {
        return String.join("", Files.readAllLines(Path.of(TOWN_PATH)));
    }

    @Override
    public String importTowns() throws IOException {
       StringBuilder builder = new StringBuilder();
        TownImportDto[] townImportDtos = this.gson.fromJson(readTownsFileContent(), TownImportDto[].class);

        for (TownImportDto townImportDto : townImportDtos) {
            if (validationUtil.isValid(townImportDto)){
                Town town = this.modelMapper.map(townImportDto,Town.class);
                builder.append(String.format("Successfully imported Town %s - %d",town.getName(),town.getPopulation()))
                        .append(System.lineSeparator());
                this.townRepository.saveAndFlush(town);
            }else {
                builder.append("Invalid Town").append(System.lineSeparator());
            }
        }

        return builder.toString();
    }
}
