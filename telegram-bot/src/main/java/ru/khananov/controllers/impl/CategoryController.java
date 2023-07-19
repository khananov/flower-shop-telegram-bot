package ru.khananov.controllers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.khananov.controllers.TelegramController;
import ru.khananov.models.domains.menukeyboard.MyCategoriesMenuKeyboardMarkup;
import ru.khananov.services.CategoryService;

import static ru.khananov.models.domains.Command.*;

@Controller
public class CategoryController implements TelegramController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public boolean support(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            return (update.getMessage().getText().equals(CATALOG_COMMAND.getValue()));
        }

        return false;
    }

    @Override
    public void execute(Update update) {
        sendCategories(update.getMessage().getChatId());
    }

    private void sendCategories(Long chatId) {
        categoryService.senCategories(chatId,
                MyCategoriesMenuKeyboardMarkup.getCategoriesMenuReplyKeyboardMarkup());
    }
}
