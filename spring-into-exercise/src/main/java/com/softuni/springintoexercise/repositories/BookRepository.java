package com.softuni.springintoexercise.repositories;

import com.softuni.springintoexercise.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByReleaseDateAfter(LocalDate localDate);

    @Query("SELECT b FROM Book b " +
            "WHERE b.author.firstName = 'George' and b.author.lastName = 'Powell' " +
            "ORDER BY b.releaseDate DESC, b.title")
    List<Book> getAllBooksWithAuthorGeorgePowell();
}
