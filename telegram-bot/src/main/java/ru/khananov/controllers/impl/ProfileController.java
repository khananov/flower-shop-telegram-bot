package ru.khananov.controllers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.khananov.controllers.TelegramController;
import ru.khananov.models.domains.inlinekeyboard.MyRegistrationInlineKeyboard;
import ru.khananov.models.domains.menukeyboard.MyGeneralMenuKeyboardMarkup;
import ru.khananov.models.domains.menukeyboard.MyVerifyCodeMenuKeyboardMarkup;
import ru.khananov.services.RegistrationService;
import ru.khananov.services.TelegramService;
import ru.khananov.services.TelegramUserService;

import static ru.khananov.models.domains.Command.*;

@Controller
public class ProfileController implements TelegramController {
    private final TelegramUserService telegramUserService;
    private final RegistrationService registrationService;
    private final TelegramService telegramService;

    @Autowired
    public ProfileController(TelegramUserService telegramUserService,
                             RegistrationService registrationService,
                             TelegramService telegramService) {
        this.telegramUserService = telegramUserService;
        this.registrationService = registrationService;
        this.telegramService = telegramService;
    }

    @Override
    public boolean support(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            return (update.getMessage().getText().equals(PROFILE_COMMAND.getValue()) ||
                    update.getMessage().getText().equals(REGISTRATION_COMMAND.getValue()) ||
                    update.getMessage().getText().equals(VERIFY_EMAIL_COMMAND.getValue()) ||
                    update.getMessage().getText().equals(DELETE_PROFILE_COMMAND.getValue()) ||
                    update.getMessage().getText().equals(CHANGE_PROFILE_COMMAND.getValue()));
        }

        return false;
    }

    @Override
    public void execute(Update update) {
        if (update.getMessage().getText().equals(PROFILE_COMMAND.getValue()))
            sendUserProfile(update.getMessage().getChatId());
        else if (update.getMessage().getText().equals(REGISTRATION_COMMAND.getValue()))
            registration(update.getMessage().getChatId());
        else if (update.getMessage().getText().equals(VERIFY_EMAIL_COMMAND.getValue()))
            verifyEmail(update.getMessage().getChatId());
        else if (update.getMessage().getText().equals(CHANGE_PROFILE_COMMAND.getValue()))
            changeProfile(update.getMessage().getChatId());
        else if (update.getMessage().getText().equals(DELETE_PROFILE_COMMAND.getValue()))
            deleteProfile(update.getMessage());
    }

    private void sendUserProfile(Long chatId) {
        telegramUserService.sendProfileMessage(chatId);
    }

    private void registration(Long chatId) {
        telegramService.deleteReplyKeyboard(chatId);
        registrationService.sendNameInlineKeyboard(chatId,
                MyRegistrationInlineKeyboard.getRegistrationKeyboardMarkup());
    }

    private void verifyEmail(Long chatId) {
        registrationService.sendCodeInlineKeyboard(chatId,
                MyVerifyCodeMenuKeyboardMarkup.getVerifyCodeMenuReplyKeyboardMarkup());
    }

    private void changeProfile(Long chatId) {
        registration(chatId);
    }

    private void deleteProfile(Message message) {
        telegramUserService.deleteProfile(message.getChatId());
        telegramUserService.saveNewUser(message);
        telegramService.sendReplyKeyboard(MyGeneralMenuKeyboardMarkup.getGeneralMenuReplyKeyboardMarkup(),
                "Информация удалена",
                message.getChatId());
    }
}