package ru.khananov.controllers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.AnswerPreCheckoutQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.payments.SuccessfulPayment;
import ru.khananov.controllers.TelegramController;
import ru.khananov.models.domains.menukeyboard.MyGeneralMenuKeyboardMarkup;
import ru.khananov.services.OrderService;
import ru.khananov.services.TelegramService;

@Controller
public class PaymentController implements TelegramController {
    private final TelegramService telegramService;
    private final OrderService orderService;

    @Autowired
    public PaymentController(TelegramService telegramService, OrderService orderService) {
        this.telegramService = telegramService;
        this.orderService = orderService;
    }

    @Override
    public boolean support(Update update) {
        if (update.hasMessage() && update.getMessage().hasSuccessfulPayment())
            return true;

        return update.hasPreCheckoutQuery();
    }

    @Override
    public void execute(Update update) {
        if (update.hasPreCheckoutQuery())
            answerToPreCheckout(update.getPreCheckoutQuery().getId());
        else if (update.hasMessage() && update.getMessage().hasSuccessfulPayment())
            successfulPayment(update.getMessage().getChatId(), update.getMessage().getSuccessfulPayment());
    }

    private void answerToPreCheckout(String preCheckoutId) {
        AnswerPreCheckoutQuery preCheckoutQuery = new AnswerPreCheckoutQuery();
        preCheckoutQuery.setPreCheckoutQueryId(preCheckoutId);
        preCheckoutQuery.setOk(true);

        telegramService.sendAnswerPreCheckoutQuery(preCheckoutQuery);
    }

    private void successfulPayment(Long chatId, SuccessfulPayment successfulPayment) {
        orderService.updateOrderStatusToPaid(successfulPayment);
        telegramService.sendReplyKeyboard(MyGeneralMenuKeyboardMarkup.getGeneralMenuReplyKeyboardMarkup(),
                "Заказ №" + successfulPayment.getInvoicePayload() + " оплачен",
                chatId);
    }
}