package ru.khananov.controllers;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface TelegramController {
    boolean support(String command);

    void execute(Update update);
}
