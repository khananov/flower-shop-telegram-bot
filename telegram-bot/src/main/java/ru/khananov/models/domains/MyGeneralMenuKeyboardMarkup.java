package ru.khananov.models.domains;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Arrays;
import java.util.Collections;

import static org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton.builder;
import static ru.khananov.models.domains.Command.*;

public final class MyGeneralMenuKeyboardMarkup {
    private static final ReplyKeyboardMarkup generalMenuReplyKeyboardMarkup = createGeneralMenuKeyboard();

    private MyGeneralMenuKeyboardMarkup() {
    }

    public static ReplyKeyboardMarkup getGeneralMenuReplyKeyboardMarkup() {
        return generalMenuReplyKeyboardMarkup;
    }

    private static ReplyKeyboardMarkup createGeneralMenuKeyboard() {
        ReplyKeyboardMarkup.ReplyKeyboardMarkupBuilder keyboardBuilder = ReplyKeyboardMarkup.builder();
        keyboardBuilder.resizeKeyboard(true);
        keyboardBuilder.selective(true);

        keyboardBuilder.keyboardRow(new KeyboardRow(Arrays.asList(
                builder().text(CATALOG_COMMAND.getValue()).build(),
                builder().text(CART_COMMAND.getValue()).build())));

        keyboardBuilder.keyboardRow(new KeyboardRow(Collections.singletonList(
                builder().text(PROFILE_COMMAND.getValue()).build())));

        return keyboardBuilder.build();
    }
}
