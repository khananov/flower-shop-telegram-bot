package ru.khananov.services.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.khananov.models.entities.TelegramUser;
import ru.khananov.repositories.TelegramUserRepository;
import ru.khananov.services.TelegramUserService;

@Log4j2
@Service
public class TelegramUserServiceImpl implements TelegramUserService {
    private final TelegramUserRepository telegramUserRepository;

    @Autowired
    public TelegramUserServiceImpl(TelegramUserRepository telegramUserRepository) {
        this.telegramUserRepository = telegramUserRepository;
    }

    @Override
    public TelegramUser registerUser(Message message) {
        TelegramUser telegramUser = telegramUserRepository.findByChatId(message.getChatId());

        if (telegramUser == null) {
            return telegramUserRepository.save(buildTelegramUser(message));
        } else {
            telegramUser.setIsActive(true);
            return telegramUserRepository.save(telegramUser);
        }
    }

    private TelegramUser buildTelegramUser(Message message) {
        return TelegramUser.builder().
                chatId(message.getChatId()).
                firstName(message.getChat().getFirstName()).
                lastName(message.getChat().getLastName()).
                username(message.getChat().getUserName()).
                isActive(true).build();
    }
}
