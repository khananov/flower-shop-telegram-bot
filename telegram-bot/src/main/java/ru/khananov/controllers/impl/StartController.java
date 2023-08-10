package ru.khananov.controllers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.khananov.controllers.TelegramController;
import ru.khananov.models.domains.menukeyboard.MyGeneralMenuKeyboardMarkup;
import ru.khananov.services.TelegramService;
import ru.khananov.services.TelegramUserService;

import static ru.khananov.models.domains.Command.*;

@Controller
public class StartController implements TelegramController {
    private final TelegramService telegramService;
    private final TelegramUserService telegramUserService;

    @Autowired
    public StartController(TelegramService telegramService, TelegramUserService telegramUserService) {
        this.telegramService = telegramService;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public boolean support(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            return (update.getMessage().getText().equals(START_COMMAND.getValue()) ||
                    update.getMessage().getText().equals(MAIN_MENU_COMMAND.getValue()));
        }

        return false;
    }

    @Override
    public void execute(Update update) {
        if (update.getMessage().getText().equals(START_COMMAND.getValue()))
            createUser(update.getMessage());
        else if (update.getMessage().getText().equals(MAIN_MENU_COMMAND.getValue()))
            startMessage(update.getMessage().getChatId());
    }

    private void startMessage(Long chatId) {
        telegramService.sendReplyKeyboard(MyGeneralMenuKeyboardMarkup.getGeneralMenuReplyKeyboardMarkup(),
                "Выберите действие",
                chatId);
    }

    private void createUser(Message message) {
        telegramUserService.saveNewUser(message);
        startMessage(message.getChatId());
    }
}