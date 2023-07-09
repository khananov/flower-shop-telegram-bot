package ru.khananov.controllers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.khananov.controllers.TelegramController;
import ru.khananov.models.entities.Category;
import ru.khananov.models.entities.Product;
import ru.khananov.services.CategoryService;
import ru.khananov.services.ProductService;
import ru.khananov.services.TelegramService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton.builder;
import static ru.khananov.models.domains.Command.*;

@Controller
public class CatalogController implements TelegramController {
    private final TelegramService telegramService;
    private final ProductService productService;

    @Autowired
    public CatalogController(TelegramService telegramService,
                             ProductService productService) {
        this.telegramService = telegramService;
        this.productService = productService;
    }

    @Override
    public boolean support(String command) {
        return (command.equals(WILD_FLOWERS_COMMAND.getValue()) ||
                command.equals(GARDEN_FLOWERS_COMMAND.getValue()));
    }

    @Override
    public void execute(Update update) {
        sendProductsByCategory(update);
    }

    private void sendProductsByCategory(Update update) {
        List<Product> products = productService.findAllByCategoryName(update.getMessage().getText());

        products.forEach(product -> telegramService.sendPhoto(createPhoto(
                update.getMessage().getChatId(),
                product)));
    }

    private SendPhoto createPhoto(Long chatId, Product product) {
        SendPhoto photo = new SendPhoto();
        photo.setChatId(chatId);

        photo.setPhoto(new InputFile(new File(product.getPhoto())));

        photo.setReplyMarkup(InlineKeyboardMarkup.builder()
                .keyboardRow(Collections.singletonList(
                        InlineKeyboardButton.builder()
                                .text("Стоимость " +
                                        productService.findPriceProduct(product) + " " +
                                        BUY_COMMAND.getValue())
                                .callbackData("Купить").build()

                )).build());

        photo.setCaption(product.getName() + "\n" + product.getDescription());

        return photo;
    }
}
