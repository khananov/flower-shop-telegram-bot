package ru.khananov.services.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.khananov.exceptions.SendMessageException;
import ru.khananov.models.entities.Category;
import ru.khananov.services.CategoryService;
import ru.khananov.services.TelegramService;

import java.util.Arrays;
import java.util.Collections;

import static org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton.builder;
import static ru.khananov.commands.Commands.*;

@Log4j2
@Service
public class TelegramServiceImpl extends DefaultAbsSender implements TelegramService {
    private final CategoryService categoryService;

    @Autowired
    public TelegramServiceImpl(@Value("${telegram-bot.token}") String botToken, CategoryService categoryService) {
        super(new DefaultBotOptions(), botToken);
        this.categoryService = categoryService;
    }

    @Override
    public void sendMessage(SendMessage message) {
        if (message != null) {
            try {
                execute(message);
            } catch (TelegramApiException e) {
                log.error(new SendMessageException("Failed send text message: " + e.getMessage()));
            }
        }
    }

    @Override
    public void sendMenu(Long chatId) {
        ReplyKeyboardMarkup keyboardMarkup = createGeneralMenuKeyboard();

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Выберите действие");
        message.setReplyMarkup(keyboardMarkup);

        sendMessage(message);
    }

    @Override
    public void sendCategoriesKeyboardMarkup(Long chatId) {
        SendMessage keyboardMarkupMessage = new SendMessage();
        keyboardMarkupMessage.setChatId(chatId);
        keyboardMarkupMessage.setReplyMarkup(createCategoriesKeyboardMarkup());
        keyboardMarkupMessage.setText("Выберите категорию");

        sendMessage(keyboardMarkupMessage);
    }

    private ReplyKeyboardMarkup createGeneralMenuKeyboard() {
        ReplyKeyboardMarkup.ReplyKeyboardMarkupBuilder keyboardBuilder = ReplyKeyboardMarkup.builder();
        keyboardBuilder.resizeKeyboard(true);
        keyboardBuilder.selective(true);

        keyboardBuilder.keyboardRow(new KeyboardRow(Arrays.asList(
                builder().text(CATALOG_COMMAND.getValue()).build(),
                builder().text(CART_COMMAND.getValue()).build())));

        keyboardBuilder.keyboardRow(new KeyboardRow(Arrays.asList(
                builder().text(PROFILE_COMMAND.getValue()).build())));

        return keyboardBuilder.build();
    }

    private InlineKeyboardMarkup createCategoriesKeyboardMarkup() {
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder keyboardBuilder = InlineKeyboardMarkup.builder();

        for (Category category : categoryService.findAll()) {
            String categoryName = category.getName();
            String categoryId = category.getId().toString();

            keyboardBuilder.keyboardRow(Collections.singletonList(
                    InlineKeyboardButton.builder().text(categoryName).callbackData(categoryId).build()
            ));
        }

        return keyboardBuilder.build();
    }
}
