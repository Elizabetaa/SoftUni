package softuni.library.services.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.library.models.dtos.jsons.AuthorImportDto;
import softuni.library.models.entities.Author;
import softuni.library.repositories.AuthorRepository;
import softuni.library.services.AuthorService;
import softuni.library.util.ValidatorUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class AuthorServiceImpl implements AuthorService {
    private final static String AUTHOR_PATH ="src/main/resources/files/json/authors.json";
    private final Gson gson;
    private final ValidatorUtil validatorUtil;
    private final ModelMapper modelMapper;
    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorServiceImpl(Gson gson, ValidatorUtil validatorUtil, ModelMapper modelMapper, AuthorRepository authorRepository) {
        this.gson = gson;
        this.validatorUtil = validatorUtil;
        this.modelMapper = modelMapper;
        this.authorRepository = authorRepository;
    }

    @Override
    public boolean areImported() {
        return this.authorRepository.count() > 0;
    }

    @Override
    public String readAuthorsFileContent() throws IOException {
        return String.join("", Files.readAllLines(Path.of(AUTHOR_PATH)));
    }

    @Override
    public String importAuthors() throws IOException {
        StringBuilder builder = new StringBuilder();

        AuthorImportDto[] authorImportDtos = this.gson.fromJson(readAuthorsFileContent(),AuthorImportDto[].class);

        for (AuthorImportDto authorImportDto : authorImportDtos) {
            Author isExisting = this.authorRepository
                    .findByFirstNameAndLastName(authorImportDto.getFirstName(),authorImportDto.getLastName());

            if (validatorUtil.isValid(authorImportDto) && isExisting == null){
                Author map = this.modelMapper.map(authorImportDto,Author.class);

                this.authorRepository.saveAndFlush(map);
                builder.append(String.format("Successfully imported Author: %s %s - %s%n",
                        map.getFirstName(),map.getLastName(),map.getBirthTown()));
            }else {
                builder.append("Invalid Author").append(System.lineSeparator());
            }
        }

        return builder.toString();
    }
}
