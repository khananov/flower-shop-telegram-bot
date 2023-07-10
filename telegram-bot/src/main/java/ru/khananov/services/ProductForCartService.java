package ru.khananov.services;

import ru.khananov.models.entities.Order;
import ru.khananov.models.entities.Product;

public interface ProductForCartService {
    void addProductForCartToOrder(Order order, Product product);
}
