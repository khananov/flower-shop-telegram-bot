package ru.khananov.controllers.impl.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.khananov.controllers.TelegramController;
import ru.khananov.models.domains.MyRegistrationInlineKeyboard;
import ru.khananov.models.domains.registration.WaitingName;
import ru.khananov.services.RegistrationService;
import ru.khananov.services.TelegramService;

import static ru.khananov.models.domains.Command.CONFIRM_COMMAND;
import static ru.khananov.models.domains.Command.REJECT_COMMAND;

@Controller
public class RegistrationNameController implements TelegramController {
    private final TelegramService telegramService;
    private final RegistrationService registrationService;

    @Autowired
    public RegistrationNameController(TelegramService telegramService,
                                      RegistrationService registrationService) {
        this.telegramService = telegramService;
        this.registrationService = registrationService;
    }

    @Override
    public boolean support(Update update) {
        if (update.hasCallbackQuery())
            return ((update.getCallbackQuery().getData().equals(CONFIRM_COMMAND.getValue()) ||
                update.getCallbackQuery().getData().equals(REJECT_COMMAND.getValue())));
        if (update.hasMessage())
            return (update.getMessage().hasText() && WaitingName.getInstance().getWaitingNameText());

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
            waitingName(update.getCallbackQuery().getMessage().getChatId());
        }
        else if (update.hasMessage() && update.getMessage().hasText()) {
            setName(update.getMessage().getChatId(), update.getMessage().getText());
        }
    }

    private void sendNextStep(Long chatId) {
        registrationService.sendAddressInlineKeyboard(chatId,
                MyRegistrationInlineKeyboard.getRegistrationKeyboardMarkup());
    }

    private void waitingName(Long chatId) {
        WaitingName.getInstance().setWaitingNameText(true);
        telegramService.sendMessage(new SendMessage(chatId.toString(), "Введите Ваше имя:"));
    }

    private void setName(Long chatId, String address) {
        registrationService.setName(chatId, address);
        WaitingName.getInstance().setWaitingNameText(false);
        sendNextStep(chatId);
    }
}
