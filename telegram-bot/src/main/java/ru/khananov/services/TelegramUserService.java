package ru.khananov.services;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.khananov.models.entities.TelegramUser;

public interface TelegramUserService {
    TelegramUser findByChatId(Long chatId);

    TelegramUser registerUser(Message message);

    void sendProfileMessage(Long chatId);

    void deleteProfile(Long chatId);
}
