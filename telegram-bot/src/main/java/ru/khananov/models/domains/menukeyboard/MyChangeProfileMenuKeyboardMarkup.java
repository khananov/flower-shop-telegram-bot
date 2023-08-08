package ru.khananov.models.domains.menukeyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Arrays;
import java.util.Collections;

import static org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton.builder;
import static ru.khananov.models.domains.Command.*;

public class MyChangeProfileMenuKeyboardMarkup {
    private static final ReplyKeyboardMarkup changeProfileMenuReplyKeyboardMarkup =
            createChangeProfileMenuKeyboard();

    private MyChangeProfileMenuKeyboardMarkup() {
    }

    public static ReplyKeyboardMarkup getChangeProfileMenuReplyKeyboardMarkup() {
        return changeProfileMenuReplyKeyboardMarkup;
    }

    private static ReplyKeyboardMarkup createChangeProfileMenuKeyboard() {
        ReplyKeyboardMarkup.ReplyKeyboardMarkupBuilder keyboardBuilder = ReplyKeyboardMarkup.builder();
        keyboardBuilder.resizeKeyboard(true);
        keyboardBuilder.selective(true);

        keyboardBuilder.keyboardRow(new KeyboardRow(Arrays.asList(
                builder().text(VERIFY_EMAIL_COMMAND.getValue()).build(),
                builder().text(CHANGE_PROFILE_COMMAND.getValue()).build())));

        keyboardBuilder.keyboardRow(new KeyboardRow(Arrays.asList(
                builder().text(DELETE_PROFILE_COMMAND.getValue()).build(),
                builder().text(GET_ORDERS_COMMAND.getValue()).build())));

        keyboardBuilder.keyboardRow(new KeyboardRow(Collections.singletonList(
                builder().text(MAIN_MENU_COMMAND.getValue()).build())));

        return keyboardBuilder.build();
    }
}