package ru.khananov.services;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public interface TelegramService {
    void sendMessage(SendMessage message);

    void sendMenu(Long chatId);

    ReplyKeyboardMarkup createGeneralMenuKeyboard();
}
