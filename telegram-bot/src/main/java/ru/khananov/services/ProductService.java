package ru.khananov.services;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.khananov.models.entities.Product;

import java.util.List;

public interface ProductService {
    List<Product> findAll();

    List<Product> findAllByCategoryName(String name);

    String findPriceProduct(Product product);

    void sendProductsByCategory(Update update);

    Product findByName(String name);
}
