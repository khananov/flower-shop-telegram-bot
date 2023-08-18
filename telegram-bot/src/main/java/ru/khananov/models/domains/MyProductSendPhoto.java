package ru.khananov.models.domains;

import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.khananov.models.entities.Product;

import java.util.Collections;

import static ru.khananov.models.domains.Command.BUY_COMMAND;

public class MyProductSendPhoto {
    private final SendPhoto sendPhoto;

    public MyProductSendPhoto(Long chatId, Product product, String price) {
        sendPhoto = createPhoto(chatId, product, price);
    }

    public SendPhoto getSendPhoto() {
        return sendPhoto;
    }

    private SendPhoto createPhoto(Long chatId, Product product, String price) {
        SendPhoto photo = new SendPhoto();
        photo.setChatId(chatId);
        photo.setPhoto(new InputFile(product.getPhoto()));

        photo.setReplyMarkup(InlineKeyboardMarkup.builder()
                .keyboardRow(Collections.singletonList(
                        InlineKeyboardButton.builder()
                                .text("Стоимость " +
                                        price + " " + BUY_COMMAND.getValue())
                                .callbackData(product.getName()).build()

                )).build());

        photo.setCaption(product.getName() + "\n" + product.getDescription());

        return photo;
    }
}