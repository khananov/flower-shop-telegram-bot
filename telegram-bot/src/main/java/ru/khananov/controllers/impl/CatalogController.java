package ru.khananov.controllers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.khananov.controllers.TelegramController;
import ru.khananov.services.ProductService;

import static ru.khananov.models.domains.Command.*;

@Controller
public class CatalogController implements TelegramController {
    private final ProductService productService;

    @Autowired
    public CatalogController(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public boolean support(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            return (update.getMessage().getText().equals(WILD_FLOWERS_COMMAND.getValue()) ||
                    update.getMessage().getText().equals(GARDEN_FLOWERS_COMMAND.getValue()));
        }

        return false;
    }

    @Override
    public void execute(Update update) {
        sendProductsByCategory(update.getMessage().getChatId(), update.getMessage().getText());
    }

    private void sendProductsByCategory(Long chatId, String categoryName) {
        productService.sendProductsByCategory(chatId, categoryName);
    }
}
