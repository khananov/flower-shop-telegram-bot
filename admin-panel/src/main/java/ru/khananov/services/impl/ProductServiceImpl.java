package ru.khananov.services.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.khananov.exceptions.ProductNotFoundException;
import ru.khananov.models.entities.Product;
import ru.khananov.repositories.ProductRepository;
import ru.khananov.services.ProductService;

import java.util.List;

@Service
@Log4j2
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseGet(() -> {
                    log.error(new ProductNotFoundException(id));
                    throw new ProductNotFoundException(id);
                });
    }

    @Override
    public void removeById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> findByNameStartingWith(String name) {
        return productRepository.findByNameStartingWithIgnoreCase(name);
    }
}
