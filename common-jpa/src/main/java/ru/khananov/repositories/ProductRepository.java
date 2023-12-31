package ru.khananov.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.khananov.models.entities.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByCategoryName(String name);

    Optional<Product> findByName(String name);

    List<Product> findByNameStartingWithIgnoreCase(String name);
}
