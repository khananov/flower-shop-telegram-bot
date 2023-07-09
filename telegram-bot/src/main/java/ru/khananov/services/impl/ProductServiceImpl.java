package ru.khananov.services.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.khananov.models.entities.Product;
import ru.khananov.repositories.ProductRepository;
import ru.khananov.services.ProductService;

import java.text.DecimalFormat;
import java.util.List;

@Log4j2
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findAllByCategoryName(String name) {
        return productRepository.findAllByCategoryName(name);
    }

    @Override
    public String findPriceProduct(Product product) {
        return new DecimalFormat("#0.00").format(convertToRub(product));
    }

    private Double convertToRub(Product product) {
        return  (double) (product.getPrice() / 100);
    }
}
