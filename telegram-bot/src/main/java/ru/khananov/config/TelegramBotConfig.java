package ru.khananov.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.khananov.utils.CryptoTool;

@Configuration
public class TelegramBotConfig {
    @Value("${salt}")
    private String salt;

    @Bean
    public TelegramBotsApi telegramBotsApi(TelegramPolling telegramPolling) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(telegramPolling);
        return telegramBotsApi;
    }

    @Bean
    public CryptoTool getCryptoTool() {
        return new CryptoTool(salt);
    }
}