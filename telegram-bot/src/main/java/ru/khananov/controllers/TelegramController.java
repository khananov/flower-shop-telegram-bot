package ru.khananov.controllers;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface TelegramController {
    boolean support(Update update);

    void execute(Update update);
}
