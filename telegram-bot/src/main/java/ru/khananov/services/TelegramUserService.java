package ru.khananov.services;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.khananov.models.entities.TelegramUser;

public interface TelegramUserService {
    TelegramUser registerUser(Message message);
}
