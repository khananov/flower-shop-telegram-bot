package ru.khananov.models.domains.inlinekeyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Arrays;

import static ru.khananov.models.domains.Command.CONFIRM_COMMAND;
import static ru.khananov.models.domains.Command.REJECT_COMMAND;

public class MyRegistrationInlineKeyboard {
    private static final InlineKeyboardMarkup registrationKeyboardMarkup =
            createRegistrationKeyboardMarkup();

    private MyRegistrationInlineKeyboard() {
    }

    public static InlineKeyboardMarkup getRegistrationKeyboardMarkup() {
        return registrationKeyboardMarkup;
    }

    private static InlineKeyboardMarkup createRegistrationKeyboardMarkup() {
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder keyboardBuilder = InlineKeyboardMarkup.builder();

        keyboardBuilder.keyboardRow(Arrays.asList(
                InlineKeyboardButton.builder().text(CONFIRM_COMMAND.getValue())
                        .callbackData(CONFIRM_COMMAND.getValue()).build(),
                InlineKeyboardButton.builder().text(REJECT_COMMAND.getValue())
                        .callbackData(REJECT_COMMAND.getValue()).build()
        ));

        return keyboardBuilder.build();
    }
}