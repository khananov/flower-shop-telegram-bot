package ru.khananov.services.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.khananov.exceptions.SendMessageException;
import ru.khananov.services.TelegramService;

@Service
@Log4j2
public class TelegramServiceImpl extends DefaultAbsSender implements TelegramService {
    protected TelegramServiceImpl(@Value("${telegram-bot.token}") String botToken) {
        super(new DefaultBotOptions(), botToken);
    }

    @Override
    public void sendMessage(SendMessage message) {
        if (message != null) {
            try {
                execute(message);
            } catch (TelegramApiException e) {
                log.error(new SendMessageException("Failed send text message: " + e.getMessage()));
            }
        }
    }
}
