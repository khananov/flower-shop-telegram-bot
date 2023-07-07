package ru.khananov.services.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.khananov.models.entities.Product;
import ru.khananov.repositories.CategoryRepository;
import ru.khananov.repositories.ProductRepository;
import ru.khananov.services.ProductService;

import java.util.List;

@Log4j2
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Product> findAllByCategoryId(CallbackQuery callbackQuery) {
        Long categoryId = Long.valueOf(callbackQuery.getData());
        return productRepository.findAllByCategoryId(categoryId);
    }
}
