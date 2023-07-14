package ru.khananov.controllers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.khananov.controllers.TelegramController;
import ru.khananov.models.domains.menukeyboard.MyProfileMenuKeyboardMarkup;
import ru.khananov.services.TelegramUserService;

import static ru.khananov.models.domains.Command.PROFILE_COMMAND;

@Controller
public class ProfileController implements TelegramController {
    private final TelegramUserService telegramUserService;

    @Autowired
    public ProfileController(TelegramUserService telegramUserService) {
        this.telegramUserService = telegramUserService;
    }

    @Override
    public boolean support(Update update) {
        if (!update.hasMessage()) return false;

        return update.getMessage().getText().equals(PROFILE_COMMAND.getValue());
    }

    @Override
    public void execute(Update update) {
        sendUserProfile(update.getMessage().getChatId());
    }

    private void sendUserProfile(Long chatId) {
        telegramUserService.sendProfileMessage(chatId,
                MyProfileMenuKeyboardMarkup.getProfileMenuReplyKeyboardMarkup());
    }
}
