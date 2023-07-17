package ru.khananov.services;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public interface RegistrationService {
    void sendNameInlineKeyboard(Long chatId, InlineKeyboardMarkup keyboardMarkup);

    void sendAddressInlineKeyboard(Long chatId, InlineKeyboardMarkup keyboardMarkup);

    void sendEmailInlineKeyboard(Long chatId, InlineKeyboardMarkup keyboardMarkup);

    void sendCodeInlineKeyboard(Long chatId, ReplyKeyboardMarkup keyboardMarkup);

    void setUserInfo(Long chatId, String info);

    void codeCheck(Long chatId, String inputCode);
}
