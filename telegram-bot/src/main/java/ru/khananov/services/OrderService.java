package ru.khananov.services;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.khananov.models.entities.Order;

public interface OrderService {
    Order findLastOrderByChatId(Long chatId);

    void sendAllOrdersByChatId(Long chatId);

    void cancelOrder(Message message);

    void payForOrder(Long chatId);

    void updateOrderStatusToPaid(String orderId);

    boolean checkTotalAmountOrder(String orderId, Integer totalAmountPurchase);
}