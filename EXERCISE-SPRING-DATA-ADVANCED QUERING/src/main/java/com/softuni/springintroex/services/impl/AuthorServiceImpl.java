package com.softuni.springintroex.services.impl;

import com.softuni.springintroex.constants.GlobalConstants;
import com.softuni.springintroex.domain.entities.Author;
import com.softuni.springintroex.domain.repositories.AuthorRepository;
import com.softuni.springintroex.services.AuthorService;
import com.softuni.springintroex.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {
    private final FileUtil fileUtil;
    private final AuthorRepository authorRepository;
    @Autowired
    public AuthorServiceImpl(FileUtil fileUtil, AuthorRepository authorRepository) {
        this.fileUtil = fileUtil;
        this.authorRepository = authorRepository;
    }

    @Override
    public void seedAuthorsInDB() throws IOException {
        String [] lines = fileUtil.readFileContent(GlobalConstants.AUTHORS_FILE_PATH);

        for (String line : lines) {
            String firstName = line.split("\\s+")[0];
            String lastName = line.split("\\s+")[1];
            Author author = new Author(firstName,lastName);
            System.out.println(author);
            this.authorRepository.saveAndFlush(author);
        }
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
    public List<Author> getAllAuthorsWhoseFirstNameEndsWith(String letter) {
        return this.authorRepository.getAllByFirstNameEndingWith(letter);
    }

    @Override
    public Author getAllAuthorsWhoseLastNameStartsWith(String pattern) {
        return this.authorRepository.getAllByLastNameStartingWith(pattern);
    }

    @Override
    public List<Author> getAllAuthors() {
        return this.authorRepository.getAllBy();
    }
}
