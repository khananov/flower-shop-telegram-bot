package ru.khananov.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.khananov.models.entities.ProductForCart;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductForCartRepository extends JpaRepository<ProductForCart, Long> {
    Optional<ProductForCart> findByProductId(Long id);

    List<ProductForCart> findAllByOrderId(Long orderId);
}
