package ru.khananov.services.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.khananov.models.entities.Order;
import ru.khananov.models.entities.TelegramUser;
import ru.khananov.repositories.OrderRepository;
import ru.khananov.services.OrderService;
import ru.khananov.services.TelegramUserService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.khananov.models.enums.OrderStatus.NEW;

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
    public Order findLastOrderByChatId(Long chatId) {
        TelegramUser telegramUser = telegramUserService.findByChatId(chatId);
        List<Order> orders = orderRepository.findAllByTelegramUserId(telegramUser.getId());
        Order lastOrder = orders.stream().filter(o -> o.getOrderStatus() == NEW).findFirst().orElse(null);

        if (lastOrder == null)
            lastOrder = createOrder(telegramUser);

        orderRepository.save(lastOrder);

        return lastOrder;
    }

    private Order createOrder(TelegramUser telegramUser) {
        return Order.builder()
                .telegramUser(telegramUser)
                .orderStatus(NEW)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
