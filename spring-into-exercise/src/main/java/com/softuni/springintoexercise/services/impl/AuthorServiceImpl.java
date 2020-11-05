package com.softuni.springintoexercise.services.impl;

import com.softuni.springintoexercise.constants.GlobalConstants;
import com.softuni.springintoexercise.entities.Author;
import com.softuni.springintoexercise.entities.Book;
import com.softuni.springintoexercise.repositories.AuthorRepository;
import com.softuni.springintoexercise.services.AuthorService;
import com.softuni.springintoexercise.utils.FileUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final FileUtil fileUtil;

    public AuthorServiceImpl(AuthorRepository authorRepository, FileUtil fileUtil) {
        this.authorRepository = authorRepository;
        this.fileUtil = fileUtil;
    }

    @Override
    public void seedAuthors() throws IOException {
        if (this.authorRepository.count() != 0) {
            return;
        }
        String[] fileContent = this.fileUtil
                .readFileContent(GlobalConstants.AUTHORS_FILE_PATH);

        Arrays.stream(fileContent)
                .forEach(r -> {
                    String[] params = r.split("\\s+");
                    Author author = new Author(params[0], params[1]);

                    this.authorRepository.saveAndFlush(author);

                });
    }

    @Override
    public int getAllAuthorsCount() {
        return (int) this.authorRepository.count();
    }

    @Override
    public Author findAuthorById(Long id) {
        return this.authorRepository
                .getOne(id);
    }

    @Override
    public List<Author> findAllAuthorsByCountOfBooks() {
        return this.authorRepository.findAuthorByCountOfBooks();
    }

    @Override
    public List<Author> getALlAuthorsWithBookBefore1990() {
        List<Author>authors = this.authorRepository.findAll();

        List<Author> authorsWithBooks = new ArrayList<>();

        for (Author author : authors) {
            Set<Book> books = author.getBooks();
            for (Book book : books) {
                if (book.getReleaseDate().getYear() < 1990) {
                    authorsWithBooks.add(author);
                    break;
                }
            }
        }
        return authorsWithBooks;
    }

}
