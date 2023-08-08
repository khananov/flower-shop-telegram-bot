package ru.khananov.models.domains.inlinekeyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Collections;

import static ru.khananov.models.domains.Command.*;

public class MyCancelOrderInlineKeyboardMarkup {
    private static final InlineKeyboardMarkup cancelOrderKeyboardMarkup =
            createCancelOrderKeyboardMarkup();

    private MyCancelOrderInlineKeyboardMarkup() {
    }

    public static InlineKeyboardMarkup getCancelOrderKeyboardMarkup() {
        return cancelOrderKeyboardMarkup;
    }

    private static InlineKeyboardMarkup createCancelOrderKeyboardMarkup() {
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder keyboardBuilder = InlineKeyboardMarkup.builder();

        keyboardBuilder.keyboardRow(Collections.singletonList(
                InlineKeyboardButton.builder().text(CANCEL_ORDER_COMMAND.getValue())
                        .callbackData(CANCEL_ORDER_COMMAND.getValue()).build()
        ));

        return keyboardBuilder.build();
    }
}