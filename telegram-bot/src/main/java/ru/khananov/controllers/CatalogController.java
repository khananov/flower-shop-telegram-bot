package ru.khananov.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.khananov.models.entities.Product;
import ru.khananov.services.ProductService;
import ru.khananov.services.TelegramService;

import java.util.List;

@Controller
public class CatalogController {
    private final TelegramService telegramService;
    private final ProductService productService;

    @Autowired
    public CatalogController(TelegramService telegramService, ProductService productService) {
        this.telegramService = telegramService;
        this.productService = productService;
    }

    public void sendCategories(Update update) {
        telegramService.sendCategoriesKeyboardMarkup(update.getMessage().getChatId());
    }

    public void sendProductsByCategory(Update update, CallbackQuery callbackQuery) {
        List<Product> products = productService.findAllByCategoryId(callbackQuery);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
        sendMessage.setText(products.get(0).getName() + "\n" + products.get(0).getDescription());
        telegramService.sendMessage(sendMessage);
    }
}
