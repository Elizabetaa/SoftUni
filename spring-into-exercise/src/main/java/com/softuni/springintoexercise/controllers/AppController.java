package com.softuni.springintoexercise.controllers;

import com.softuni.springintoexercise.services.AuthorService;
import com.softuni.springintoexercise.services.BookService;
import com.softuni.springintoexercise.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Controller
public class AppController implements CommandLineRunner {

    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final BookService bookService;

    @Autowired
    public AppController(CategoryService categoryService, AuthorService authorService, BookService bookService) {
        this.categoryService = categoryService;
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @Override
    public void run(String... args) throws Exception {
        this.categoryService.seedCategories();
        this.authorService.seedAuthors();
        this.bookService.seedBooks();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println();
        System.out.println("Write which exercise want to check 1, 2, 3 or 4:");

        int ex = Integer.parseInt(reader.readLine());

        switch (ex) {
            case 1:
                this.bookService.getAllBooksAfter2000().forEach(b -> {
                    System.out.println("Title: " + b.getTitle());
                });
                break;
            case 2:
                this.authorService
                        .getALlAuthorsWithBookBefore1990()
                        .forEach(a -> {
                            System.out.printf("Full name: %s %s%n", a.getFirstName(),
                                    a.getLastName());
                        });
                break;
            case 3:
                this.authorService
                        .findAllAuthorsByCountOfBooks()
                        .forEach(a -> {
                            System.out.printf("Full name: %s %s, Book count:%d %n", a.getFirstName(),
                                    a.getLastName(),
                                    a.getBooks().size());
                        });
                break;
            case 4:
                this.bookService
                        .getAllBooksWithAuthorGeorgePowell()
                        .forEach(b -> {
                            System.out.printf("Title: %s, Release date: %s, Copies: %d%n",
                                    b.getTitle(),
                                    b.getReleaseDate(),
                                    b.getCopies());
                        });
                break;
            default:
                System.out.println("Invalid Exercise!!!");
        }

    }
}
