package ru.khananov.models.domains;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Arrays;
import java.util.Collections;

import static org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton.builder;
import static ru.khananov.models.domains.Command.*;

public class MyCartMenuKeyboardMarkup {
    private static final ReplyKeyboardMarkup cartReplyKeyboardMarkup = createCartMenuKeyboard();

    private MyCartMenuKeyboardMarkup() {
    }

    public static ReplyKeyboardMarkup getCartReplyKeyboardMarkup() {
        return cartReplyKeyboardMarkup;
    }

    private static ReplyKeyboardMarkup createCartMenuKeyboard() {
        ReplyKeyboardMarkup.ReplyKeyboardMarkupBuilder keyboardBuilder = ReplyKeyboardMarkup.builder();
        keyboardBuilder.resizeKeyboard(true);
        keyboardBuilder.selective(true);

        keyboardBuilder.keyboardRow(new KeyboardRow(Arrays.asList(
                builder().text(CREATE_ORDER_COMMAND.getValue()).build(),
                builder().text(CLEAR_ORDER_COMMAND.getValue()).build())));

        keyboardBuilder.keyboardRow(new KeyboardRow(Collections.singletonList(
                builder().text(MAIN_MENU_COMMAND.getValue()).build())));

        return keyboardBuilder.build();
    }
}
