package ru.khananov.controllers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.khananov.controllers.TelegramController;
import ru.khananov.services.ProductForCartService;


import static ru.khananov.models.domains.Command.MINUS_AMOUNT_COMMAND;
import static ru.khananov.models.domains.Command.PLUS_AMOUNT_COMMAND;

@Controller
public class ProductInOrderController implements TelegramController {
    private final ProductForCartService productForCartService;

    @Autowired
    public ProductInOrderController(ProductForCartService productForCartService) {
        this.productForCartService = productForCartService;
    }

    @Override
    public boolean support(Update update) {
        if (update.hasCallbackQuery()) {
            return (update.getCallbackQuery().getData().equals(MINUS_AMOUNT_COMMAND.getValue()) ||
                    update.getCallbackQuery().getData().equals(PLUS_AMOUNT_COMMAND.getValue()));
        }

        return false;
    }

    @Override
    public void execute(Update update) {
        if (update.getCallbackQuery().getData().equals(MINUS_AMOUNT_COMMAND.getValue()))
            minusAmount(update.getCallbackQuery().getMessage());
        else if (update.getCallbackQuery().getData().equals(PLUS_AMOUNT_COMMAND.getValue()))
            plusAmount(update.getCallbackQuery().getMessage());
    }

    private void minusAmount(Message message) {
        productForCartService.minusAmount(message);
    }

    private void plusAmount(Message message) {
        productForCartService.plusAmount(message);
    }
}