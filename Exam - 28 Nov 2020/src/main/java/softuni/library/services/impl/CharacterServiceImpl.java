package softuni.library.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.library.models.dtos.xmls.CharacterImportDto;
import softuni.library.models.dtos.xmls.CharactersImportRootDto;
import softuni.library.models.entities.Book;
import softuni.library.models.entities.Character;
import softuni.library.repositories.BookRepository;
import softuni.library.repositories.CharacterRepository;
import softuni.library.services.CharacterService;
import softuni.library.util.ValidatorUtil;
import softuni.library.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class CharacterServiceImpl implements CharacterService {
    private final static String CHARACTER_PATH ="src/main/resources/files/xml/characters.xml";
    private final BookRepository bookRepository;
    private final CharacterRepository characterRepository;
    private final ValidatorUtil validatorUtil;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;

    @Autowired
    public CharacterServiceImpl(BookRepository bookRepository, CharacterRepository characterRepository, ValidatorUtil validatorUtil, XmlParser xmlParser, ModelMapper modelMapper) {
        this.bookRepository = bookRepository;
        this.characterRepository = characterRepository;
        this.validatorUtil = validatorUtil;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean areImported() {
        return this.characterRepository.count() > 0;
    }

    @Override
    public String readCharactersFileContent() throws IOException {
        return String.join("", Files.readAllLines(Path.of(CHARACTER_PATH)));
    }

    @Override
    public String importCharacters() throws JAXBException {
        StringBuilder builder = new StringBuilder();
        CharactersImportRootDto charactersImportRootDto = this.xmlParser.parseXml(CharactersImportRootDto.class, CHARACTER_PATH);

        for (CharacterImportDto characterImportDto : charactersImportRootDto.getCharacterImportDtos()) {
            Optional<Book> book = this.bookRepository.findById(characterImportDto.getBook().getId());

            if (validatorUtil.isValid(characterImportDto) && book.isPresent()){
                Character character = this.modelMapper.map(characterImportDto,Character.class);
                character.setBook(book.get());

                this.characterRepository.saveAndFlush(character);
                builder.append(String.format("Successfully imported Character %s %s %s - %d%n",
                        character.getFirstName(),character.getMiddleName(),character.getLastName(),
                        character.getAge()));

            }else {
                builder.append("Invalid Character").append(System.lineSeparator());
            }
        }

        return builder.toString();
    }

    @Override
    public String findCharactersInBookOrderedByLastNameDescendingThenByAge() {
        StringBuilder builder = new StringBuilder();
        this.characterRepository.getAllCharactersWithAgeAfter32Ordered().forEach(c -> {
            builder.append(String.format("Character name %s %s %s, age %d, in book %s%n",
                    c.getFirstName(),c.getMiddleName(),c.getLastName(),c.getAge(),c.getBook().getName()
                    ));
        });

        return builder.toString();
    }
}
