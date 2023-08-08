package ru.khananov.services.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.khananov.exceptions.OrderNotFoundException;
import ru.khananov.models.entities.Order;
import ru.khananov.repositories.OrderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.khananov.models.enums.OrderStatus.DELIVERED;
import static ru.khananov.models.enums.OrderStatus.NEW;

@Service
@Log4j2
public class OrderServiceImpl implements ru.khananov.services.OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll().stream()
                .filter(order -> !order.getOrderStatus().equals(NEW)).collect(Collectors.toList());
    }

    @Override
    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseGet(() -> {
                    log.error(new OrderNotFoundException(id));
                    throw new OrderNotFoundException(id);
                });
    }

    @Override
    public void delivered(Long id) {
        Order order = findById(id);
        order.setOrderStatus(DELIVERED);
        orderRepository.save(order);
    }

    @Override
    public List<Order> findByIdStartingWith(String id) {
        if (id == null) return new ArrayList<>();

        return orderRepository.findByIdStartingWith(id);
    }
}
