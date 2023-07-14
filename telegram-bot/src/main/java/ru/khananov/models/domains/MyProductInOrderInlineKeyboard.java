package ru.khananov.models.domains;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Arrays;

import static ru.khananov.models.domains.Command.MINUS_AMOUNT_COMMAND;
import static ru.khananov.models.domains.Command.PLUS_AMOUNT_COMMAND;

public final class MyProductInOrderInlineKeyboard {
    private static final InlineKeyboardMarkup productsInOrderKeyboardMarkup =
            createProductsInOrderKeyboardMarkup();

    private MyProductInOrderInlineKeyboard() {
    }

    public static InlineKeyboardMarkup getProductsInOrderKeyboardMarkup() {
        return productsInOrderKeyboardMarkup;
    }

    private static InlineKeyboardMarkup createProductsInOrderKeyboardMarkup() {
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder keyboardBuilder = InlineKeyboardMarkup.builder();

        keyboardBuilder.keyboardRow(Arrays.asList(
                InlineKeyboardButton.builder().text(MINUS_AMOUNT_COMMAND.getValue())
                        .callbackData(MINUS_AMOUNT_COMMAND.getValue()).build(),
                InlineKeyboardButton.builder().text(PLUS_AMOUNT_COMMAND.getValue())
                        .callbackData(PLUS_AMOUNT_COMMAND.getValue()).build()
        ));

        return keyboardBuilder.build();
    }
}
