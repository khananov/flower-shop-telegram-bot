package ru.khananov.services.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.khananov.models.domains.inlinekeyboard.MyRegistrationInlineKeyboard;
import ru.khananov.models.domains.registration.WaitingAddress;
import ru.khananov.models.domains.registration.WaitingEmail;
import ru.khananov.models.domains.registration.WaitingName;
import ru.khananov.models.entities.TelegramUser;
import ru.khananov.repositories.TelegramUserRepository;
import ru.khananov.services.RegistrationService;
import ru.khananov.services.TelegramService;
import ru.khananov.services.TelegramUserService;

@Log4j2
@Service
public class RegistrationServiceImpl implements RegistrationService {
    private final TelegramUserService telegramUserService;
    private final TelegramUserRepository telegramUserRepository;
    private final TelegramService telegramService;

    @Autowired
    public RegistrationServiceImpl(TelegramUserService telegramUserService,
                                   TelegramUserRepository telegramUserRepository,
                                   TelegramService telegramService) {
        this.telegramUserService = telegramUserService;
        this.telegramUserRepository = telegramUserRepository;
        this.telegramService = telegramService;
    }

    @Override
    public void sendNameInlineKeyboard(Long chatId, InlineKeyboardMarkup keyboardMarkup) {
        TelegramUser user = telegramUserRepository.findByChatId(chatId);
        if (user.getFirstName() == null) {
            telegramService.sendMessage(new SendMessage(chatId.toString(), "Введите Ваше имя:"));
        } else {
            telegramService.sendInlineKeyboard(MyRegistrationInlineKeyboard.getRegistrationKeyboardMarkup(),
                    "Ваше имя - " + user.getFirstName() + "?", chatId);
        }

        WaitingName.getInstance().setWaitingNameText(true);
    }

    @Override
    public void sendAddressInlineKeyboard(Long chatId, InlineKeyboardMarkup keyboardMarkup) {
        TelegramUser user = telegramUserRepository.findByChatId(chatId);
        if (user.getAddress() == null) {
            telegramService.sendMessage(new SendMessage(chatId.toString(), "Введите Ваш адрес:"));
        } else {
            telegramService.sendInlineKeyboard(MyRegistrationInlineKeyboard.getRegistrationKeyboardMarkup(),
                    "Ваш адрес - " + user.getAddress() + "?", chatId);
        }

        WaitingAddress.getInstance().setWaitingAddressText(true);
    }

    @Override
    public void sendEmailInlineKeyboard(Long chatId, InlineKeyboardMarkup keyboardMarkup) {
        TelegramUser user = telegramUserRepository.findByChatId(chatId);
        if (user.getEmail() == null) {
            telegramService.sendMessage(new SendMessage(chatId.toString(), "Введите Ваш email:"));
        } else {
            telegramService.sendInlineKeyboard(keyboardMarkup,
                    "Ваш email - " + user.getEmail() + "?", chatId);
        }

        WaitingEmail.getInstance().setWaitingEmailText(true);
    }

    @Override
    public void setName(Long chatId, String name) {
        TelegramUser user = telegramUserService.findByChatId(chatId);
        user.setFirstName(name);
        telegramUserRepository.save(user);
    }

    @Override
    public void setAddress(Long chatId, String address) {
        TelegramUser user = telegramUserService.findByChatId(chatId);
        user.setAddress(address);
        telegramUserRepository.save(user);
    }

    @Override
    public void setEmail(Long chatId, String email) {
        TelegramUser user = telegramUserService.findByChatId(chatId);
        user.setEmail(email);
        telegramUserRepository.save(user);
    }
}
