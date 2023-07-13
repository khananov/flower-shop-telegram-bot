package ru.khananov.services.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.khananov.models.entities.Order;
import ru.khananov.models.entities.TelegramUser;
import ru.khananov.models.enums.OrderStatus;
import ru.khananov.repositories.OrderRepository;
import ru.khananov.services.OrderService;
import ru.khananov.services.TelegramUserService;

import java.time.LocalDateTime;

@Log4j2
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final TelegramUserService telegramUserService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            TelegramUserService telegramUserService) {
        this.orderRepository = orderRepository;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public Order findOrderByChatId(Long chatId) {
        TelegramUser telegramUser = telegramUserService.findByChatId(chatId);
        Order order;

        if (telegramUser.getOrder() == null)
            order = createOrder(telegramUser);
        else
            order = orderRepository.findByTelegramUserId(telegramUser.getId());

        orderRepository.save(order);

        return order;
    }

    private Order createOrder(TelegramUser telegramUser) {
        return Order.builder()
                .telegramUser(telegramUser)
                .orderStatus(OrderStatus.NEW)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
