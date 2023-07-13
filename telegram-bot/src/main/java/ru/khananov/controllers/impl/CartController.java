package ru.khananov.controllers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.khananov.controllers.TelegramController;
import ru.khananov.models.domains.MyCartMenuKeyboardMarkup;
import ru.khananov.models.domains.MyGeneralMenuKeyboardMarkup;
import ru.khananov.services.CartService;
import ru.khananov.services.ProductForCartService;
import ru.khananov.services.TelegramService;

import static ru.khananov.models.domains.Command.CART_COMMAND;
import static ru.khananov.models.domains.Command.CLEAR_ORDER_COMMAND;

@Controller
public class CartController implements TelegramController {
    private final TelegramService telegramService;
    private final CartService cartService;
    private final ProductForCartService productForCartService;

    @Autowired
    public CartController(TelegramService telegramService,
                          CartService cartService,
                          ProductForCartService productForCartService) {
        this.telegramService = telegramService;
        this.cartService = cartService;
        this.productForCartService = productForCartService;
    }

    @Override
    public boolean support(Update update) {
        if (!update.hasMessage()) return false;

        return (update.getMessage().getText().equals(CART_COMMAND.getValue()) ||
                update.getMessage().getText().equals(CLEAR_ORDER_COMMAND.getValue()));
    }

    @Override
    public void execute(Update update) {
        if (update.getMessage().getText().equals(CART_COMMAND.getValue())) {
            sendCart(update.getMessage());
            sendProductsInOrder(update.getMessage());
        } else if (update.getMessage().getText().equals(CLEAR_ORDER_COMMAND.getValue()))
            clearOrder(update.getMessage());
    }

    private void sendCart(Message message) {
        if (productForCartService.findAllByChatId(message.getChatId()).isEmpty())
            telegramService.sendMessage(new SendMessage(message.getChatId().toString(), "Корзина пуста"));
        else
            telegramService.sendReplyKeyboard(MyCartMenuKeyboardMarkup.getCartReplyKeyboardMarkup(),
                "Ваш заказ:", message.getChatId());
    }

    private void sendProductsInOrder(Message message) {
        cartService.sendProductsInOrder(message.getChatId());
    }

    private void clearOrder(Message message) {
        cartService.clearOrder(message.getChatId());
        telegramService.sendReplyKeyboard(MyGeneralMenuKeyboardMarkup.getGeneralMenuReplyKeyboardMarkup(),
                "Корзина очищена", message.getChatId());
    }
}
