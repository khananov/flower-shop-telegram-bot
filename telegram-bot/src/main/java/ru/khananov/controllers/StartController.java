package ru.khananov.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.khananov.services.TelegramService;
import ru.khananov.services.TelegramUserService;

@Controller
public class StartController {
    private final TelegramService telegramService;
    private final TelegramUserService telegramUserService;

    @Autowired
    public StartController(TelegramService telegramService, TelegramUserService telegramUserService) {
        this.telegramService = telegramService;
        this.telegramUserService = telegramUserService;
    }

    public void startMethod(Update update) {
        telegramUserService.registerUser(update.getMessage());
        telegramService.sendMenu(update.getMessage().getChatId());
    }
}
