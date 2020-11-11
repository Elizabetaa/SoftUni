package com.softuni.springintroex.services;

import com.softuni.springintroex.domain.entities.Category;

import java.io.IOException;

public interface CategoryService {

    void seedCategoriesInDB() throws IOException;

    Category getCategoryById(long categoryId);
}
