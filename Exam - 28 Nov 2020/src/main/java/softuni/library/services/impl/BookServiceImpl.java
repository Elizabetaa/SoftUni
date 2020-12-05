package softuni.library.services.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.library.models.dtos.jsons.BooksImportDto;
import softuni.library.models.entities.Author;
import softuni.library.models.entities.Book;
import softuni.library.repositories.AuthorRepository;
import softuni.library.repositories.BookRepository;
import softuni.library.services.BookService;
import softuni.library.util.ValidatorUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {
    private final static String BOOK_PATH ="src/main/resources/files/json/books.json";
    private final Gson gson;
    private final ValidatorUtil validatorUtil;
    private final ModelMapper modelMapper;
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(Gson gson, ValidatorUtil validatorUtil, ModelMapper modelMapper, AuthorRepository authorRepository, BookRepository bookRepository) {
        this.gson = gson;
        this.validatorUtil = validatorUtil;
        this.modelMapper = modelMapper;
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public boolean areImported() {
        return this.bookRepository.count() > 0;
    }

    @Override
    public String readBooksFileContent() throws IOException {
        return String.join("", Files.readAllLines(Path.of(BOOK_PATH)));
    }

    @Override
    public String importBooks() throws IOException {
        StringBuilder builder = new StringBuilder();
        BooksImportDto[] booksImportDtos = this.gson.fromJson(readBooksFileContent(), BooksImportDto[].class);

        for (BooksImportDto booksImportDto : booksImportDtos) {
            Book book = this.modelMapper.map(booksImportDto,Book.class);
            Optional<Author> author = this.authorRepository.findById(booksImportDto.getAuthor());
            if (validatorUtil.isValid(booksImportDto) && author.isPresent()){
                book.setAuthor(author.get());
                this.bookRepository.saveAndFlush(book);
                builder.append(String
                        .format("Successfully imported Book: %s written in %s%n",
                                book.getName(),book.getWritten()));
            }else {
                builder.append("Invalid Book").append(System.lineSeparator());
            }
        }

        return builder.toString();
    }
}
