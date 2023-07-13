package ru.khananov.services.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.khananov.exceptions.SendMessageException;
import ru.khananov.services.TelegramService;

@Log4j2
@Service
public class TelegramServiceImpl extends DefaultAbsSender implements TelegramService {

    @Autowired
    public TelegramServiceImpl(@Value("${telegram-bot.token}") String botToken) {
        super(new DefaultBotOptions(), botToken);
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

    public void sendEditMessage(EditMessageText message) {
        if (message != null) {
            try {
                execute(message);
            } catch (TelegramApiException e) {
                log.error(new SendMessageException("Failed send text message: " + e.getMessage()));
            }
        }
    }

    @Override
    public void sendPhoto(SendPhoto photo) {
        if (photo != null) {
            try {
                execute(photo);
            } catch (TelegramApiException e) {
                log.error(new SendMessageException("Failed send photo message: " + e.getMessage()));
            }
        }
    }

    @Override
    public void sendReplyKeyboard(ReplyKeyboardMarkup keyboardMarkup, String text, Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        message.setReplyMarkup(keyboardMarkup);

        sendMessage(message);
    }

    @Override
    public void sendInlineKeyboard(InlineKeyboardMarkup keyboardMarkup, String text, Long chatId) {
        SendMessage keyboardMarkupMessage = new SendMessage();
        keyboardMarkupMessage.setChatId(chatId);
        keyboardMarkupMessage.setReplyMarkup(keyboardMarkup);
        keyboardMarkupMessage.setText(text);

        sendMessage(keyboardMarkupMessage);
    }

    @Override
    public void deleteMessage(DeleteMessage message) {
        if (message != null) {
            try {
                execute(message);
            } catch (TelegramApiException e) {
                log.error(new SendMessageException("Failed delete message: " + e.getMessage()));
            }
        }
    }
}
