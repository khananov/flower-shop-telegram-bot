package ru.khananov.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.khananov.models.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Long, Product> {
}
