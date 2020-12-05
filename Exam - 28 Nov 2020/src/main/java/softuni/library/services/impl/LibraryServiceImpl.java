package softuni.library.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.library.models.dtos.xmls.LibrariesImportRootDto;
import softuni.library.models.dtos.xmls.LibraryImportDto;
import softuni.library.models.entities.Book;
import softuni.library.models.entities.Library;
import softuni.library.repositories.BookRepository;
import softuni.library.repositories.CharacterRepository;
import softuni.library.repositories.LibraryRepository;
import softuni.library.services.LibraryService;
import softuni.library.util.ValidatorUtil;
import softuni.library.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class LibraryServiceImpl implements LibraryService {
    private final static String LIBRARY_PATH = "src/main/resources/files/xml/libraries.xml";
    private final BookRepository bookRepository;
    private final ValidatorUtil validatorUtil;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final LibraryRepository libraryRepository;

    @Autowired
    public LibraryServiceImpl(BookRepository bookRepository, ValidatorUtil validatorUtil, XmlParser xmlParser, ModelMapper modelMapper, LibraryRepository libraryRepository) {
        this.bookRepository = bookRepository;
        this.validatorUtil = validatorUtil;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.libraryRepository = libraryRepository;
    }

    @Override
    public boolean areImported() {
        return this.libraryRepository.count() > 0;
    }

    @Override
    public String readLibrariesFileContent() throws IOException {
        return String.join("", Files.readAllLines(Path.of(LIBRARY_PATH)));
    }

    @Override
    public String importLibraries() throws JAXBException {
        StringBuilder builder = new StringBuilder();
        LibrariesImportRootDto librariesImportRootDto = this.xmlParser.parseXml(LibrariesImportRootDto.class, LIBRARY_PATH);

        for (LibraryImportDto libraryImportDto : librariesImportRootDto.getLibraryImportDtos()) {
            Optional<Book> book = this.bookRepository.findById(libraryImportDto.getBook().getId());

            if (validatorUtil.isValid(libraryImportDto) && book.isPresent()) {
                Library map = this.modelMapper.map(libraryImportDto, Library.class);
                Set<Book> books = new LinkedHashSet<>();
                if (map.getBooks() == null) {
                    books.add(book.get());
                }else {
                    books = map.getBooks();
                }
                map.setBooks(books);


                this.libraryRepository.saveAndFlush(map);

                builder.append(String.format("Successfully added Library: %s - %s%n",
                        map.getName(), map.getLocation()));
            } else {
                builder.append("Invalid Library").append(System.lineSeparator());
            }


        }

        return builder.toString();
    }
}
