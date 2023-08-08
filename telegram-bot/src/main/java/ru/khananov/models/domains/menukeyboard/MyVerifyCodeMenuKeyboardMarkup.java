package ru.khananov.models.domains.menukeyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Collections;

import static org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton.builder;
import static ru.khananov.models.domains.Command.*;

public class MyVerifyCodeMenuKeyboardMarkup {
    private static final ReplyKeyboardMarkup verifyCodeMenuReplyKeyboardMarkup = createVerifyCodeMenuKeyboard();

    private MyVerifyCodeMenuKeyboardMarkup() {
    }

    public static ReplyKeyboardMarkup getVerifyCodeMenuReplyKeyboardMarkup() {
        return verifyCodeMenuReplyKeyboardMarkup;
    }

    private static ReplyKeyboardMarkup createVerifyCodeMenuKeyboard() {
        ReplyKeyboardMarkup.ReplyKeyboardMarkupBuilder keyboardBuilder = ReplyKeyboardMarkup.builder();
        keyboardBuilder.resizeKeyboard(true);
        keyboardBuilder.selective(true);

        keyboardBuilder.keyboardRow(new KeyboardRow(Collections.singletonList(
                builder().text(CANCEL_CODE_COMMAND.getValue()).build())));

        return keyboardBuilder.build();
    }
}