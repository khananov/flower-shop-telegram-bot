package ru.khananov.controllers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.khananov.controllers.TelegramController;
import ru.khananov.models.domains.menukeyboard.MyCartMenuKeyboardMarkup;
import ru.khananov.models.domains.menukeyboard.MyGeneralMenuKeyboardMarkup;
import ru.khananov.services.CartService;
import ru.khananov.services.OrderService;
import ru.khananov.services.ProductForCartService;
import ru.khananov.services.TelegramService;

import static ru.khananov.models.domains.Command.*;

@Controller
public class CartController implements TelegramController {
    private final TelegramService telegramService;
    private final CartService cartService;
    private final ProductForCartService productForCartService;
    private final OrderService orderService;

    @Autowired
    public CartController(TelegramService telegramService,
                          CartService cartService,
                          ProductForCartService productForCartService, OrderService orderService) {
        this.telegramService = telegramService;
        this.cartService = cartService;
        this.productForCartService = productForCartService;
        this.orderService = orderService;
    }

    @Override
    public boolean support(Update update) {
        if (update.hasCallbackQuery())
            return update.getCallbackQuery().getData().equals(CANCEL_ORDER_COMMAND.getValue());

        if (update.hasMessage() && update.getMessage().hasText())
            return (update.getMessage().getText().equals(CART_COMMAND.getValue()) ||
                update.getMessage().getText().equals(CLEAR_ORDER_COMMAND.getValue()) ||
                update.getMessage().getText().equals(PAY_ORDER_COMMAND.getValue()) ||
                update.getMessage().getText().equals(GET_ORDERS_COMMAND.getValue()));

        return false;
    }

    @Override
    public void execute(Update update) {
        if (update.hasCallbackQuery())
            cancelOrder(update.getCallbackQuery().getMessage());
        else if (update.getMessage().getText().equals(CART_COMMAND.getValue())) {
            sendCart(update.getMessage().getChatId());
            sendProductsInOrder(update.getMessage().getChatId());
        } else if (update.getMessage().getText().equals(CLEAR_ORDER_COMMAND.getValue()))
            clearOrder(update.getMessage().getChatId());
        else if (update.getMessage().getText().equals(PAY_ORDER_COMMAND.getValue()))
            payForOrder(update.getMessage().getChatId());
        else if (update.getMessage().getText().equals(GET_ORDERS_COMMAND.getValue()))
            sendOrders(update.getMessage().getChatId());
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

    private void payForOrder(Long chatId) {
        orderService.payForOrder(chatId);
    }

    private void sendOrders(Long chatId) {
        orderService.sendAllOrdersByChatId(chatId);
    }

    private void cancelOrder(Message message) {
        orderService.cancelOrder(message);
    }
}
