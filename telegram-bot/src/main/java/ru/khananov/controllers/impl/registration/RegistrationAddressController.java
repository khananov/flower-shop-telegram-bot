package ru.khananov.controllers.impl.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.khananov.controllers.TelegramController;
import ru.khananov.models.domains.MyRegistrationInlineKeyboard;
import ru.khananov.models.domains.registration.WaitingAddress;
import ru.khananov.services.RegistrationService;
import ru.khananov.services.TelegramService;

import static ru.khananov.models.domains.Command.CONFIRM_COMMAND;
import static ru.khananov.models.domains.Command.REJECT_COMMAND;

@Controller
public class RegistrationAddressController implements TelegramController {
    private final RegistrationService registrationService;
    private final TelegramService telegramService;

    @Autowired
    public RegistrationAddressController(RegistrationService registrationService,
                                         TelegramService telegramService) {
        this.registrationService = registrationService;
        this.telegramService = telegramService;
    }

    @Override
    public boolean support(Update update) {
        if (update.hasCallbackQuery())
            return ((update.getCallbackQuery().getData().equals(CONFIRM_COMMAND.getValue()) ||
                    update.getCallbackQuery().getData().equals(REJECT_COMMAND.getValue())));
        if (update.hasMessage())
            return (update.getMessage().hasText() && WaitingAddress.getInstance().getWaitingAddressText());

        return false;
    }

    @Override
    public void execute(Update update) {
        if (update.hasCallbackQuery() &&
                update.getCallbackQuery().getData().equals(CONFIRM_COMMAND.getValue())) {
            sendNextStep(update.getCallbackQuery().getMessage().getChatId());
        }
        else if (update.hasCallbackQuery() &&
                update.getCallbackQuery().getData().equals(REJECT_COMMAND.getValue())) {
            waitingAddress(update.getCallbackQuery().getMessage().getChatId());
        }
        else if (update.hasMessage() && update.getMessage().hasText()) {
            setAddress(update.getMessage().getChatId(), update.getMessage().getText());
        }
    }

    private void sendNextStep(Long chatId) {
        registrationService.sendEmailInlineKeyboard(chatId,
                MyRegistrationInlineKeyboard.getRegistrationKeyboardMarkup());
    }

    private void waitingAddress(Long chatId) {
        WaitingAddress.getInstance().setWaitingAddressText(true);
        telegramService.sendMessage(new SendMessage(chatId.toString(), "Введите Ваш адрес:"));
    }

    private void setAddress(Long chatId, String address) {
        registrationService.setAddress(chatId, address);
        WaitingAddress.getInstance().setWaitingAddressText(false);
        sendNextStep(chatId);
    }
}
