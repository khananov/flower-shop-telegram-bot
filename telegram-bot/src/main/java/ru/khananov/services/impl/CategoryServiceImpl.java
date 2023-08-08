package ru.khananov.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ru.khananov.services.CategoryService;
import ru.khananov.services.TelegramService;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final TelegramService telegramService;

    @Autowired
    public CategoryServiceImpl(TelegramService telegramService) {
        this.telegramService = telegramService;
    }

    @Override
    public void senCategories(Long chatId, ReplyKeyboardMarkup keyboardMarkup) {
        telegramService.sendReplyKeyboard(
                keyboardMarkup,
                "Выберите категорию",
                chatId);
    }
}
