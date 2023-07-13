package ru.khananov.controllers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.khananov.controllers.TelegramController;
import ru.khananov.models.entities.Order;
import ru.khananov.models.entities.Product;
import ru.khananov.services.*;

@Controller
public class BuyController implements TelegramController {
    private final TelegramService telegramService;
    private final ProductService productService;
    private final ProductForCartService productForCartService;
    private final OrderService orderService;

    @Autowired
    public BuyController(TelegramService telegramService,
                         ProductService productService,
                         ProductForCartService productForCartService,
                         OrderService orderService) {
        this.telegramService = telegramService;
        this.productService = productService;
        this.productForCartService = productForCartService;
        this.orderService = orderService;
    }

    @Override
    public boolean support(Update update) {
        if (!update.hasCallbackQuery()) return false;

        return productService.findAll().stream()
                .anyMatch(product -> product.getName().equals(update.getCallbackQuery().getData()));
    }

    @Override
    public void execute(Update update) {
        addProductToOrder(update.getCallbackQuery());
    }

    private void addProductToOrder(CallbackQuery callbackQuery) {
        Order order = orderService.findOrderByChatId(callbackQuery.getMessage().getChatId());
        Product product = productService.findByName(callbackQuery.getData());
        productForCartService.addProductForCartToOrder(order, product);

        telegramService.sendMessage(new SendMessage(callbackQuery.getMessage().getChatId().toString(),
                "Добавлено в корзину - " + product.getName()));
    }
}
