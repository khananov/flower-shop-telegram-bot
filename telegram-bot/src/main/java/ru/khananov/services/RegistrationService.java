package ru.khananov.services;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public interface RegistrationService {
    void sendNameInlineKeyboard(Long chatId, InlineKeyboardMarkup keyboardMarkup);

    void sendAddressInlineKeyboard(Long chatId, InlineKeyboardMarkup keyboardMarkup);

    void sendEmailInlineKeyboard(Long chatId, InlineKeyboardMarkup keyboardMarkup);

    void setName(Long chatId, String name);

    void setAddress(Long chatId, String address);

    void setEmail(Long chatId, String email);
}
