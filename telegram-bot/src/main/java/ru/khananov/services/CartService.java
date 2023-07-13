package ru.khananov.services;

import ru.khananov.models.entities.ProductForCart;

public interface CartService {
    void sendProductsInOrder(Long chatId);

    void sendEditMessageProductInOrder(Long chatId, Integer messageId, ProductForCart productForCart);

    void clearOrder(Long chatId);
}
