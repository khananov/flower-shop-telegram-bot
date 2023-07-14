package ru.khananov.services;

import ru.khananov.models.entities.Order;

public interface OrderService {
    Order findLastOrderByChatId(Long chatId);
}
