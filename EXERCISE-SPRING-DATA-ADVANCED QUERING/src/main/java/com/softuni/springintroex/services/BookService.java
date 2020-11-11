package com.softuni.springintroex.services;

import com.softuni.springintroex.domain.entities.AgeRestriction;
import com.softuni.springintroex.domain.entities.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface BookService {
    void  seedBooksInDB() throws IOException;
    List<Book> getAllBooksByAgeRestriction(AgeRestriction ageRestriction);

    List<Book> getAllBooksWithEditionTypeGoldAndCopiesLessThan5000();

    List<Book> getAllBooksTitlesWithPriceBetween5And40();

    List<Book> getAllBooksWithReleaseDateIsNotInYear(String year);

    List<Book> getAllBeforeReleaseDate(LocalDate date);

    List<Book> getAllBooksWhoseContainingString(String pattern);

    List<Book> getAllBooksWithTitleGreaterThan(int length);

    Book getBookByTitle(String title);

}
