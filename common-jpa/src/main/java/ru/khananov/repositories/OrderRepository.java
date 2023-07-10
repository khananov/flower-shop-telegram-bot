package ru.khananov.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.khananov.models.entities.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByTelegramUserId(Long id);
}
