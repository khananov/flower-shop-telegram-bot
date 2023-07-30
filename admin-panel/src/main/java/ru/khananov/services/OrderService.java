package ru.khananov.services;

import ru.khananov.models.entities.Order;

import java.util.List;

public interface OrderService {
    List<Order> findAll();

    Order findById(Long id);

    void delivered(Long id);

    List<Order> findByIdStartingWith(String id);
}
