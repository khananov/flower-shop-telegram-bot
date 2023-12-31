package ru.khananov.services;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.khananov.dto.MailParamsDto;
import ru.khananov.models.entities.TelegramUser;
import ru.khananov.models.enums.UserStatus;

public interface TelegramUserService {
    TelegramUser findByChatId(Long chatId);

    void saveNewUser(Message message);

    void sendProfileMessage(Long chatId);

    void deleteProfile(Long chatId);

    UserStatus getUserStatusByChatId(Long chatId);

    void updateUserStatus(Long chatId, UserStatus status);

    MailParamsDto mapUserToMailParamsDto(TelegramUser user);
}