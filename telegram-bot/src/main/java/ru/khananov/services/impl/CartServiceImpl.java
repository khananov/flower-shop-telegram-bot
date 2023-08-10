package ru.khananov.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import ru.khananov.models.domains.inlinekeyboard.MyProductInOrderInlineKeyboard;
import ru.khananov.models.entities.Order;
import ru.khananov.models.entities.ProductForCart;
import ru.khananov.repositories.OrderRepository;
import ru.khananov.services.CartService;
import ru.khananov.services.OrderService;
import ru.khananov.services.TelegramService;

import java.text.DecimalFormat;

@Service
public class CartServiceImpl implements CartService {
    private final OrderRepository orderRepository;
    private final OrderService orderService;
    private final TelegramService telegramService;

    @Autowired
    public CartServiceImpl(OrderRepository orderRepository,
                           OrderService orderService,
                           TelegramService telegramService) {
        this.orderRepository = orderRepository;
        this.orderService = orderService;
        this.telegramService = telegramService;
    }

    @Override
    public void sendProductsInOrder(Long chatId) {
        Order order = orderService.findLastOrderByChatId(chatId);

        order.getProductsForCart().forEach(product -> telegramService.sendInlineKeyboard(
                MyProductInOrderInlineKeyboard.getProductsInOrderKeyboardMarkup(),
                getTextForProductInOrder(product),
                chatId));
    }

    @Override
    public void sendEditMessageProductInOrder(Long chatId, Integer messageId, ProductForCart productForCart) {
        if (productForCart == null) {
            telegramService.deleteMessage(new DeleteMessage(chatId.toString(), messageId));
        } else {
            EditMessageText editMessage = EditMessageText.builder()
                    .chatId(chatId)
                    .messageId(messageId)
                    .text(getTextForProductInOrder(productForCart))
                    .replyMarkup(MyProductInOrderInlineKeyboard.getProductsInOrderKeyboardMarkup())
                    .build();

            telegramService.sendEditMessage(editMessage);
        }
    }

    @Override
    public void clearOrder(Long chatId) {
        Order order = orderService.findLastOrderByChatId(chatId);
        orderRepository.delete(order);
    }

    private String calculateSum(ProductForCart product) {
        return new DecimalFormat("#0.00").format(
                product.getPrice() * product.getAmount() / 100);
    }

    private String getTextForProductInOrder(ProductForCart productForCart) {
        return productForCart.getProduct().getName() +
                "\n" + "Количество - " + productForCart.getAmount() + " шт.," +
                " сумма: " + calculateSum(productForCart);

    }
}
