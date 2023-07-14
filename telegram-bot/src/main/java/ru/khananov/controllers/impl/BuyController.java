package ru.khananov.controllers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.khananov.controllers.TelegramController;
import ru.khananov.services.*;

@Controller
public class BuyController implements TelegramController {
    private final TelegramService telegramService;
    private final ProductService productService;
    private final ProductForCartService productForCartService;

    @Autowired
    public BuyController(TelegramService telegramService,
                         ProductService productService,
                         ProductForCartService productForCartService) {
        this.telegramService = telegramService;
        this.productService = productService;
        this.productForCartService = productForCartService;
    }

    @Override
    public boolean support(Update update) {
        if (!update.hasCallbackQuery()) return false;

        return productService.findAll().stream()
                .anyMatch(product -> product.getName().equals(update.getCallbackQuery().getData()));
    }

    @Override
    public void execute(Update update) {
        addProductToOrder(update.getCallbackQuery().getMessage().getChatId(),
                update.getCallbackQuery().getData());
    }

    private void addProductToOrder(Long chatId, String text) {
        productForCartService.addProductForCartToOrder(chatId, text);

        telegramService.sendMessage(new SendMessage(chatId.toString(),
                "Добавлено в корзину - " + text));
    }
}
