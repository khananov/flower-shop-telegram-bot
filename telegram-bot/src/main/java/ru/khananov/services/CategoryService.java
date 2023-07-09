package ru.khananov.services;

import ru.khananov.models.entities.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findAll();

    Category findByName(String name);
}
