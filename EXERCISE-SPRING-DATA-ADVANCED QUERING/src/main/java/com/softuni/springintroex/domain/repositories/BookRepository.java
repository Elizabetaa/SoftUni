package com.softuni.springintroex.domain.repositories;

import com.softuni.springintroex.domain.entities.AgeRestriction;
import com.softuni.springintroex.domain.entities.Author;
import com.softuni.springintroex.domain.entities.Book;
import org.hibernate.sql.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByAgeRestriction(AgeRestriction ageRestriction);

    @Query("SELECT b FROM Book AS b WHERE b.editionType = 2 AND b.copies < 5000")
    List<Book> findAllByEditionTypeAndCopiesIsLessThan5000();

    List<Book> findAllByPriceBetween(BigDecimal first,BigDecimal second);

    @Query("SELECT b FROM Book AS b WHERE substring(b.releaseDate,0,4) not like :year")
    List<Book> findAllByReleaseDateIsNotInYear(String year);

    List<Book> findAllByReleaseDateBefore(LocalDate date);

    List<Book> findAllByTitleContaining(String str);

    @Query("SELECT b FROM Book AS b WHERE length(b.title) > :length ")
    List<Book> findAllByTitleGreaterThan(int length);

    Book findAllByTitle(String title);


}
