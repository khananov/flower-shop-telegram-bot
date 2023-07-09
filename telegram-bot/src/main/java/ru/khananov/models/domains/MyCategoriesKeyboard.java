package ru.khananov.models.domains;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.khananov.models.entities.Category;

import java.util.Collections;
import java.util.List;

public final class MyCategoriesKeyboard {
    private static InlineKeyboardMarkup categoriesKeyboardMarkup;

    private MyCategoriesKeyboard() {
    }

    public static InlineKeyboardMarkup getCategoriesKeyboardMarkup() {
        return categoriesKeyboardMarkup;
    }

    private static InlineKeyboardMarkup createCategoriesKeyboardMarkup(List<Category> categories) {
        if (categories == null) return new InlineKeyboardMarkup();
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder keyboardBuilder = InlineKeyboardMarkup.builder();

        for (Category category : categories) {
            String categoryName = category.getName();
            String categoryId = category.getId().toString();

            keyboardBuilder.keyboardRow(Collections.singletonList(
                    InlineKeyboardButton.builder().text(categoryName).callbackData(categoryId).build()
            ));
        }

        return keyboardBuilder.build();
    }
}
