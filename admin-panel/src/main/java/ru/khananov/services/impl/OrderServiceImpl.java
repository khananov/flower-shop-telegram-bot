package ru.khananov.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.khananov.exceptions.ProductNotFoundException;
import ru.khananov.models.entities.Order;
import ru.khananov.repositories.OrderRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.khananov.models.enums.OrderStatus.DELIVERED;
import static ru.khananov.models.enums.OrderStatus.NEW;

@Service
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
        return orderRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public void delivered(Long id) {
        Order order = findById(id);

        if (order != null) {
            order.setOrderStatus(DELIVERED);
            orderRepository.save(order);
        }
    }

    @Override
    public List<Order> findByIdStartingWith(String id) {
        return orderRepository.findByIdStartingWith(Long.valueOf(id));
    }
}
