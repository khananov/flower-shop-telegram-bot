package ru.khananov.services;

import org.telegram.telegrambots.meta.api.methods.AnswerPreCheckoutQuery;
import org.telegram.telegrambots.meta.api.methods.invoices.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.payments.PreCheckoutQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public interface TelegramService {
    void sendMessage(SendMessage message);

    void sendAnswerPreCheckoutQuery(AnswerPreCheckoutQuery preCheckoutQuery);

    void sendInvoiceMessage(SendInvoice invoice);

    void sendEditMessage(EditMessageText message);

    void sendPhoto(SendPhoto photo);

    void sendReplyKeyboard(ReplyKeyboardMarkup keyboardMarkup, String text, Long chatId);

    void sendInlineKeyboard(InlineKeyboardMarkup keyboardMarkup, String text, Long chatId);

    void deleteMessage(DeleteMessage message);

}
