package ru.khananov.services;

import ru.khananov.models.entities.Order;
import ru.khananov.models.entities.Product;
import ru.khananov.models.entities.ProductForCart;

import java.util.List;

public interface ProductForCartService {
    ProductForCart addProductForCartToOrder(Order order, Product product);

    List<ProductForCart> findAllByChatId(Long chatId);

    ProductForCart findByProductId(Long productId);

    ProductForCart plusAmount(ProductForCart productForCart);

    ProductForCart minusAmount(ProductForCart productForCart);


}
