package ru.khananov.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.khananov.dispatchers.UpdateDispatcher;

@Component
public class TelegramPolling extends TelegramLongPollingBot {
    private final UpdateDispatcher updateDispatcher;
    private final String botUsername;

    @Autowired
    public TelegramPolling(@Value("${telegram-bot.name}") String botUsername,
                           @Value("${telegram-bot.token}") String botToken,
                           UpdateDispatcher updateDispatcher) {
        super(botToken);
        this.botUsername = botUsername;
        this.updateDispatcher = updateDispatcher;
    }

    @Override
    public void onUpdateReceived(Update update) {
        updateDispatcher.processUpdate(update);
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }
}