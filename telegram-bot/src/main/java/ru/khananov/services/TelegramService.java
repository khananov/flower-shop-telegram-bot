package ru.khananov.services;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface TelegramService {
    void sendMessage(SendMessage message);
}
