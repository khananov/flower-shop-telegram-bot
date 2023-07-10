package ru.khananov.services.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.khananov.models.entities.Order;
import ru.khananov.models.entities.Product;
import ru.khananov.models.entities.ProductForCart;
import ru.khananov.repositories.ProductForCartRepository;
import ru.khananov.services.ProductForCartService;

@Log4j2
@Service
public class ProductForCartServiceImpl implements ProductForCartService {
    public final ProductForCartRepository productForCartRepository;

    @Autowired
    public ProductForCartServiceImpl(ProductForCartRepository productForCartRepository) {
        this.productForCartRepository = productForCartRepository;
    }

    @Override
    public void addProductForCartToOrder(Order order, Product product) {
        productForCartRepository.save(createProductForCart(order, product));
    }

    private ProductForCart createProductForCart(Order order, Product product) {
        return ProductForCart.builder()
                .product(product)
                .order(order)
                .price(product.getPrice())
                .amount(1L)
                .build();
    }
}
