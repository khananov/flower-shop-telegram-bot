package ru.khananov.models.domains;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Arrays;
import java.util.Collections;

import static org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton.builder;
import static ru.khananov.models.domains.Command.*;

public class MyCategoriesMenuKeyboardMarkup {
    private static final ReplyKeyboardMarkup generalMenuReplyKeyboardMarkup = createCategoriesMenuKeyboard();

    private MyCategoriesMenuKeyboardMarkup() {
    }

    public static ReplyKeyboardMarkup getGeneralMenuReplyKeyboardMarkup() {
        return generalMenuReplyKeyboardMarkup;
    }

    private static ReplyKeyboardMarkup createCategoriesMenuKeyboard() {
        ReplyKeyboardMarkup.ReplyKeyboardMarkupBuilder keyboardBuilder = ReplyKeyboardMarkup.builder();
        keyboardBuilder.resizeKeyboard(true);
        keyboardBuilder.selective(true);

        keyboardBuilder.keyboardRow(new KeyboardRow(Arrays.asList(
                builder().text(GARDEN_FLOWERS_COMMAND.getValue()).build(),
                builder().text(WILD_FLOWERS_COMMAND.getValue()).build())));

        keyboardBuilder.keyboardRow(new KeyboardRow(Collections.singletonList(
                builder().text(MAIN_MENU_COMMAND.getValue()).build())));

        return keyboardBuilder.build();
    }
}
