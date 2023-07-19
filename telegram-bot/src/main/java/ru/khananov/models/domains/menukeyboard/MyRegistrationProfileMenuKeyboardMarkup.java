package ru.khananov.models.domains.menukeyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Arrays;
import java.util.Collections;

import static org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton.builder;
import static ru.khananov.models.domains.Command.*;

public class MyRegistrationProfileMenuKeyboardMarkup {
    private static final ReplyKeyboardMarkup profileMenuReplyKeyboardMarkup = createProfileMenuKeyboard();

    private MyRegistrationProfileMenuKeyboardMarkup() {
    }

    public static ReplyKeyboardMarkup getProfileMenuReplyKeyboardMarkup() {
        return profileMenuReplyKeyboardMarkup;
    }

    private static ReplyKeyboardMarkup createProfileMenuKeyboard() {
        ReplyKeyboardMarkup.ReplyKeyboardMarkupBuilder keyboardBuilder = ReplyKeyboardMarkup.builder();
        keyboardBuilder.resizeKeyboard(true);
        keyboardBuilder.selective(true);

        keyboardBuilder.keyboardRow(new KeyboardRow(Arrays.asList(
                builder().text(REGISTRATION_COMMAND.getValue()).build(),
                builder().text(GET_ORDERS_COMMAND.getValue()).build())));

        keyboardBuilder.keyboardRow(new KeyboardRow(Collections.singletonList(
                builder().text(MAIN_MENU_COMMAND.getValue()).build())));

        return keyboardBuilder.build();
    }
}
