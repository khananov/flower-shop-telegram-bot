package ru.khananov.controllers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.khananov.controllers.TelegramController;
import ru.khananov.models.domains.MyCategoriesMenuKeyboardMarkup;
import ru.khananov.services.TelegramService;

import static ru.khananov.models.domains.Command.*;

@Controller
public class CategoryController implements TelegramController {
    private final TelegramService telegramService;

    @Autowired
    public CategoryController(TelegramService telegramService) {
        this.telegramService = telegramService;
    }

    @Override
    public boolean support(Update update) {
        if (!update.hasMessage()) return false;

        return (update.getMessage().getText().equals(CATALOG_COMMAND.getValue()));
    }

    @Override
    public void execute(Update update) {
        sendCategories(update);
    }

    private void sendCategories(Update update) {
        telegramService.sendReplyKeyboard(
                MyCategoriesMenuKeyboardMarkup.getCategoriesMenuReplyKeyboardMarkup(),
                "Выберите категорию",
                update.getMessage().getChatId());
    }
}
