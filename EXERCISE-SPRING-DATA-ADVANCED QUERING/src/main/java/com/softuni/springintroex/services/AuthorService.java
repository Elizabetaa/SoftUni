package com.softuni.springintroex.services;

import com.softuni.springintroex.domain.entities.Author;

import java.io.IOException;
import java.util.List;

public interface AuthorService {
    void seedAuthorsInDB() throws IOException;

    int getAllAuthorsCount();

    Author findAuthorById(Long id);

    List<Author> getAllAuthorsWhoseFirstNameEndsWith(String letter);

    Author getAllAuthorsWhoseLastNameStartsWith(String pattern);

    List<Author> getAllAuthors();
}
