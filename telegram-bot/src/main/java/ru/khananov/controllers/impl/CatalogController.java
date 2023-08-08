package ru.khananov.controllers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.khananov.controllers.TelegramController;
import ru.khananov.models.domains.menukeyboard.MyCategoriesMenuKeyboardMarkup;
import ru.khananov.services.CategoryService;
import ru.khananov.services.ProductService;

import static ru.khananov.models.domains.Command.*;

@Controller
public class CatalogController implements TelegramController {
    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public CatalogController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @Override
    public boolean support(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            return (update.getMessage().getText().equals(CATALOG_COMMAND.getValue()) ||
                    update.getMessage().getText().equals(WILD_FLOWERS_COMMAND.getValue()) ||
                    update.getMessage().getText().equals(GARDEN_FLOWERS_COMMAND.getValue()));
        }

        return false;
    }

    @Override
    public void execute(Update update) {
        if (update.getMessage().getText().equals(CATALOG_COMMAND.getValue()))
            sendCategories(update.getMessage().getChatId());
        else
            sendProductsByCategory(update.getMessage().getChatId(), update.getMessage().getText());
    }

    private void sendCategories(Long chatId) {
        categoryService.senCategories(chatId,
                MyCategoriesMenuKeyboardMarkup.getCategoriesMenuReplyKeyboardMarkup());
    }

    private void sendProductsByCategory(Long chatId, String categoryName) {
        productService.sendProductsByCategory(chatId, categoryName);
    }
}