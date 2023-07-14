package ru.khananov.services;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public interface CategoryService {
    void senCategories(Long chatId, ReplyKeyboardMarkup keyboardMarkup);
}
