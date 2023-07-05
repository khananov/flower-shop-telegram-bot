package ru.khananov.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.khananov.models.entities.ProductForCart;

@Repository
public interface ProductForCartRepository extends JpaRepository<Long, ProductForCart> {
}
