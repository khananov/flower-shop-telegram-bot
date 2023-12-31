package ru.khananov.controllers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.AnswerPreCheckoutQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.payments.PreCheckoutQuery;
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
            answerToPreCheckout(update.getPreCheckoutQuery());
        else if (update.hasMessage() && update.getMessage().hasSuccessfulPayment())
            successfulPayment(update.getMessage().getChatId(),
                    update.getMessage().getSuccessfulPayment().getInvoicePayload());
    }

        private void answerToPreCheckout(PreCheckoutQuery preCheckout) {
            AnswerPreCheckoutQuery answerPreCheckoutQuery = new AnswerPreCheckoutQuery();
            answerPreCheckoutQuery.setPreCheckoutQueryId(preCheckout.getId());

            if (orderService.checkTotalAmountOrder(preCheckout.getInvoicePayload(), preCheckout.getTotalAmount()))
                answerPreCheckoutQuery.setOk(true);
            else {
                answerPreCheckoutQuery.setOk(false);
                answerPreCheckoutQuery.setErrorMessage("Сумма заказа была изменена");

                telegramService.sendMessage(new SendMessage(preCheckout.getFrom().getId().toString(),
                        "Сумма заказа была изменена, оформите заказ еще раз"));
                }

            telegramService.sendAnswerPreCheckoutQuery(answerPreCheckoutQuery);
    }

    private void successfulPayment(Long chatId, String orderId) {
        orderService.updateOrderStatusToPaid(orderId);
        telegramService.sendReplyKeyboard(MyGeneralMenuKeyboardMarkup.getGeneralMenuReplyKeyboardMarkup(),
                "Заказ №" + orderId + " оплачен", chatId);
    }
}