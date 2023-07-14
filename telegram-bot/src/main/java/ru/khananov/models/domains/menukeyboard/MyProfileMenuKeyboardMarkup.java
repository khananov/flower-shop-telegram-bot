package ru.khananov.models.domains.menukeyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Arrays;

import static org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton.builder;
import static ru.khananov.models.domains.Command.*;

public class MyProfileMenuKeyboardMarkup {
    private static final ReplyKeyboardMarkup profileMenuReplyKeyboardMarkup = createProfileMenuKeyboard();

    private MyProfileMenuKeyboardMarkup() {
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
                builder().text(MAIN_MENU_COMMAND.getValue()).build())));

        return keyboardBuilder.build();
    }
}
