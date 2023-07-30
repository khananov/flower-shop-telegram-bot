package ru.khananov.services;

import ru.khananov.models.entities.Product;

import java.util.List;

public interface ProductService {
    List<Product> findAll();

    Product findById(Long id);

    void removeById(Long id);

    Product save(Product product);

    List<Product> findByNameStartingWith(String name);
}
