package ru.khananov.services;

import ru.khananov.models.entities.Product;

import java.util.List;

public interface ProductService {
    List<Product> findAll();

    List<Product> findAllByCategoryName(String name);

    String findPriceProduct(Product product);

    void sendProductsByCategory(Long chatId, String text);

    Product findByName(String name);
}