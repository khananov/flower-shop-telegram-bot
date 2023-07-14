package ru.khananov.services;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.khananov.models.entities.ProductForCart;

import java.util.List;

public interface ProductForCartService {
    ProductForCart addProductForCartToOrder(Long chatId, String productName);

    List<ProductForCart> findAllByChatId(Long chatId);

    ProductForCart findByProductId(Long productId);

    void plusAmount(Message message);

    void minusAmount(Message message);


}
