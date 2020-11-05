package com.softuni.springintoexercise.services;

import com.softuni.springintoexercise.entities.Author;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface AuthorService {
    void seedAuthors() throws IOException;

    int getAllAuthorsCount();

    Author findAuthorById(Long id);

    List<Author> findAllAuthorsByCountOfBooks();

    List<Author> getALlAuthorsWithBookBefore1990();
}
