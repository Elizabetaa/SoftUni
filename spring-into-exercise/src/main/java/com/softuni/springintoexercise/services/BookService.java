package com.softuni.springintoexercise.services;

import com.softuni.springintoexercise.entities.Book;

import java.io.IOException;
import java.util.List;

public interface BookService {
    void seedBooks() throws IOException;

    List<Book> getAllBooksAfter2000();

    List<Book> getAllBooksWithAuthorGeorgePowell();
}
