package com.softuni.springintroex;

import com.softuni.springintroex.domain.entities.AgeRestriction;
import com.softuni.springintroex.domain.entities.Author;
import com.softuni.springintroex.domain.entities.Book;
import com.softuni.springintroex.domain.repositories.AuthorRepository;
import com.softuni.springintroex.services.AuthorService;
import com.softuni.springintroex.services.BookService;
import com.softuni.springintroex.services.CategoryService;
import com.sun.xml.bind.v2.TODO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class Engine implements CommandLineRunner {
    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final BookService bookService;

    @Autowired
    public Engine(CategoryService categoryService, AuthorRepository authorRepository, AuthorService authorService, BookService bookService) {
        this.categoryService = categoryService;
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @Override
    public void run(String... args) throws Exception {
//        TODO Не забравяйте да промените username и password в application.properties !
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        seedData();

        System.out.println();
        System.out.println("Write which exercise want to check: ");
        while(true){
            int ex = 0;
            try{
                 ex = Integer.parseInt(reader.readLine());
            }catch (NumberFormatException e){
                System.exit(0);
            }
            switch (ex){
                case 1:
                    System.out.println("Write age restriction:");
                    String ageRestriction = reader.readLine().toUpperCase();
                    AgeRestriction ageRestriction1 = AgeRestriction.valueOf(ageRestriction);
                    this.bookService.getAllBooksByAgeRestriction(ageRestriction1)
                            .forEach(b -> {
                                System.out.println(b.getTitle());
                            });
                    break;
                case 2:
                    this.bookService.getAllBooksWithEditionTypeGoldAndCopiesLessThan5000()
                        .forEach(b -> {
                            System.out.println(b.getTitle());
                        });
                break;
                case 3:
                    this.bookService.getAllBooksTitlesWithPriceBetween5And40()
                            .forEach(b -> {
                                System.out.printf("%s - $%.2f%n",b.getTitle(),b.getPrice());
                            });
                    break;
                case 4:
                    System.out.println("Write year: ");
                    String year = reader.readLine();
                    this.bookService.getAllBooksWithReleaseDateIsNotInYear(year)
                            .forEach(b -> {
                                System.out.println(b.getTitle());
                            });
                    break;
                case 5:
                    System.out.println("Write date in format dd-MM-yyyy:");
                    String date = reader.readLine();
                    date = date.replaceAll("-","/");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
                    LocalDate localDate = LocalDate.parse(date, formatter);
                    this.bookService.getAllBeforeReleaseDate(localDate)
                            .forEach(b -> {
                                System.out.printf("%s %s %.2f%n",
                                        b.getTitle(),
                                        b.getEditionType(),
                                        b.getPrice());
                            });
                    break;
                case 6:
                    System.out.println("Write pattern to take author whose first name ends with this pattern:");
                    String letter = reader.readLine();
                    this.authorService.getAllAuthorsWhoseFirstNameEndsWith(letter)
                            .forEach(a -> {
                                System.out.printf("%s %s%n",a.getFirstName(),a.getLastName());
                            });
                    break;
                case 7:
                    System.out.println("Write pattern to take title of books whose contains this pattern: ");
                    String pattern = reader.readLine();
                    this.bookService.getAllBooksWhoseContainingString(pattern)
                            .forEach(b -> {
                                System.out.println(b.getTitle());
                            });
                    break;
                case 8:
                    System.out.println("Write pattern to take author whose last name starts with this pattern:");
                     pattern = reader.readLine();
                    Author author = this.authorService.getAllAuthorsWhoseLastNameStartsWith(pattern);
                    author.getBooks().forEach(b ->{
                        System.out.printf("%s (%s %s)%n",b.getTitle(),author.getFirstName(),author.getLastName());
                    });
                    break;
                case 9:
                    System.out.println("Write number to take titles of books whose is longer than this number:");
                    int length = Integer.parseInt(reader.readLine());
                    System.out.println("Count of books: " +this.bookService.getAllBooksWithTitleGreaterThan(length).size());
                    break;
                case 10:
                    this.authorService.getAllAuthors()
                            .forEach(a -> {
                                int count = a.getBooks().stream().mapToInt(Book::getCopies).sum();
                                System.out.printf("%s %s - %d%n",a.getFirstName(),a.getLastName(),count);
                            });
                    break;
                case 11:
                    System.out.println("Write title to take book with this title:");
                    String title = reader.readLine();
                    Book book = this.bookService.getBookByTitle(title);
                    System.out.printf("%s %s %s %.2f",
                            book.getTitle(),
                            book.getEditionType(),
                            book.getAgeRestriction(),
                            book.getPrice());
                    break;
                default:
                    System.out.println("Invalid exercise!!! Expect 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11");
            }
            System.out.println();
            System.out.println("-----------------------------------------");
            System.out.println("If you want to exit just print \"exit\"!");
            System.out.println("Else write which exercise want to check:");
        }
    }

    void seedData() throws IOException {
        this.categoryService.seedCategoriesInDB();
        this.authorService.seedAuthorsInDB();
        this.bookService.seedBooksInDB();
    }
}
