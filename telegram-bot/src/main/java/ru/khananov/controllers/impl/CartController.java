package ru.khananov.controllers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.khananov.controllers.TelegramController;
import ru.khananov.models.domains.menukeyboard.MyCartMenuKeyboardMarkup;
import ru.khananov.models.domains.menukeyboard.MyGeneralMenuKeyboardMarkup;
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
            sendCart(update.getMessage().getChatId());
            sendProductsInOrder(update.getMessage().getChatId());
        } else if (update.getMessage().getText().equals(CLEAR_ORDER_COMMAND.getValue()))
            clearOrder(update.getMessage().getChatId());
    }

    private void sendCart(Long chatId) {
        if (productForCartService.findAllByChatId(chatId).isEmpty())
            telegramService.sendMessage(new SendMessage(chatId.toString(), "Корзина пуста"));
        else
            telegramService.sendReplyKeyboard(MyCartMenuKeyboardMarkup.getCartReplyKeyboardMarkup(),
                "Ваш заказ:", chatId);
    }

    private void sendProductsInOrder(Long chatId) {
        cartService.sendProductsInOrder(chatId);
    }

    private void clearOrder(Long chatId) {
        cartService.clearOrder(chatId);
        telegramService.sendReplyKeyboard(MyGeneralMenuKeyboardMarkup.getGeneralMenuReplyKeyboardMarkup(),
                "Корзина очищена", chatId);
    }
}
