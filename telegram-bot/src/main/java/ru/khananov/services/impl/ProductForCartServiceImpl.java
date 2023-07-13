package ru.khananov.services.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.khananov.models.entities.Order;
import ru.khananov.models.entities.Product;
import ru.khananov.models.entities.ProductForCart;
import ru.khananov.repositories.ProductForCartRepository;
import ru.khananov.services.OrderService;
import ru.khananov.services.ProductForCartService;

import java.util.List;

@Log4j2
@Service
public class ProductForCartServiceImpl implements ProductForCartService {
    public final ProductForCartRepository productForCartRepository;
    private final OrderService orderService;

    @Autowired
    public ProductForCartServiceImpl(ProductForCartRepository productForCartRepository,
                                     OrderService orderService) {
        this.productForCartRepository = productForCartRepository;
        this.orderService = orderService;
    }

    @Override
    public ProductForCart addProductForCartToOrder(Order order, Product product) {
        ProductForCart productForCart = findByProductId(product.getId());

        if (productForCart == null)
            return productForCartRepository.save(createProductForCart(order, product));

        productForCart.setAmount(productForCart.getAmount() + 1);
        return productForCartRepository.save(productForCart);
    }

    @Override
    public List<ProductForCart> findAllByChatId(Long chatId) {
        Order order = orderService.findOrderByChatId(chatId);
        return productForCartRepository.findAllByOrderId(order.getId());
    }

    @Override
    public ProductForCart findByProductId(Long productId) {
        return productForCartRepository.findByProductId(productId);
    }

    @Override
    public ProductForCart plusAmount(ProductForCart productForCart) {
        productForCart.setAmount(productForCart.getAmount() + 1);
        return productForCartRepository.save(productForCart);
    }

    @Override
    public ProductForCart minusAmount(ProductForCart productForCart) {
        if (productForCart.getAmount() == 1) {
            productForCartRepository.delete(productForCart);
            return null;
        } else {
            productForCart.setAmount(productForCart.getAmount() - 1);
            return productForCartRepository.save(productForCart);
        }
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
