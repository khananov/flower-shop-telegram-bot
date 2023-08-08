package ru.khananov.services.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.khananov.exceptions.ProductForCartNotFoundException;
import ru.khananov.exceptions.ProductNotFoundException;
import ru.khananov.models.entities.Order;
import ru.khananov.models.entities.Product;
import ru.khananov.models.entities.ProductForCart;
import ru.khananov.repositories.ProductForCartRepository;
import ru.khananov.services.CartService;
import ru.khananov.services.OrderService;
import ru.khananov.services.ProductForCartService;
import ru.khananov.services.ProductService;

import java.util.List;
import java.util.Objects;

@Service
@Log4j2
public class ProductForCartServiceImpl implements ProductForCartService {
    private final ProductForCartRepository productForCartRepository;
    private final OrderService orderService;
    private final ProductService productService;
    private final CartService cartService;

    @Autowired
    public ProductForCartServiceImpl(ProductForCartRepository productForCartRepository,
                                     OrderService orderService,
                                     ProductService productService,
                                     CartService cartService) {
        this.productForCartRepository = productForCartRepository;
        this.orderService = orderService;
        this.productService = productService;
        this.cartService = cartService;
    }

    @Override
    public ProductForCart addProductForCartToOrder(Long chatId, String productName) {
        Order order = orderService.findLastOrderByChatId(chatId);
        Product product = productService.findByName(productName);

        ProductForCart productForCart = order.getProductsForCart().stream()
                .filter(p -> Objects.equals(p.getProduct().getId(), product.getId()))
                .findFirst().orElse(null);

        if (productForCart == null)
            return productForCartRepository.save(createProductForCart(order, product));

        productForCart.setAmount(productForCart.getAmount() + 1);

        return productForCartRepository.save(productForCart);
    }

    @Override
    public List<ProductForCart> findAllByChatId(Long chatId) {
        Order order = orderService.findLastOrderByChatId(chatId);

        return productForCartRepository.findAllByOrderId(order.getId());
    }

    @Override
    public List<ProductForCart> findAllByOrderId(Long orderId) {
        return productForCartRepository.findAllByOrderId(orderId);
    }

    @Override
    public void plusAmount(Message message) {
        int indexOfNewLine = message.getText().indexOf('\n');
        String productName = message.getText().substring(0, indexOfNewLine);

        Product product = productService.findByName(productName);
        Order order = orderService.findLastOrderByChatId(message.getChatId());

        ProductForCart productForCart = order.getProductsForCart().stream()
                .filter(p -> Objects.equals(p.getProduct().getId(), product.getId()))
                .findFirst()
                .orElseGet(() -> {
                    log.error(new ProductForCartNotFoundException(product.getId()));
                    throw new ProductForCartNotFoundException(product.getId());
                });

        productForCart.setAmount(productForCart.getAmount() + 1);
        productForCartRepository.save(productForCart);

        cartService.sendEditMessageProductInOrder(message.getChatId(), message.getMessageId(), productForCart);
    }

    @Override
    public void minusAmount(Message message) {
        int indexOfNewLine = message.getText().indexOf('\n');
        String productName = message.getText().substring(0, indexOfNewLine);

        Product product = productService.findByName(productName);
        Order order = orderService.findLastOrderByChatId(message.getChatId());

        ProductForCart productForCart = order.getProductsForCart().stream()
                .filter(p -> Objects.equals(p.getProduct().getId(), product.getId()))
                .findFirst()
                .orElseGet(() -> {
                    log.error(new ProductNotFoundException(product.getId()));
                    throw new ProductNotFoundException(product.getId());
                });

        if (productForCart.getAmount() == 1) {
            productForCartRepository.delete(productForCart);
            productForCart = null;
        } else {
            productForCart.setAmount(productForCart.getAmount() - 1);
            productForCartRepository.save(productForCart);
        }

        cartService.sendEditMessageProductInOrder(message.getChatId(), message.getMessageId(), productForCart);
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
