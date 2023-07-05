package ru.khananov.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Log4j2
@Component
public class TelegramBotConfig extends TelegramLongPollingBot {
    private final String botUsername;

    @Autowired
    public TelegramBotConfig(@Value("${telegram-bot.name}") String botUsername,
                             @Value("${telegram-bot.token}") String botToken,
                             TelegramBotsApi telegramBotsApi) throws TelegramApiException {
        super(botToken);
        this.botUsername = botUsername;
        telegramBotsApi.registerBot(this);
    }

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId());
        message.setText("Hello");
        sendMessage(message);
    }

    void sendMessage(SendMessage message) {
        if (message != null) {
            try {
                execute(message);
            } catch (TelegramApiException e) {
                log.error(e);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }
}