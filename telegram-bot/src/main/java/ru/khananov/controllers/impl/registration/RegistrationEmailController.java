package ru.khananov.controllers.impl.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.khananov.controllers.TelegramController;
import ru.khananov.models.domains.registration.WaitingEmail;
import ru.khananov.services.RegistrationService;
import ru.khananov.services.TelegramService;

import static ru.khananov.models.domains.Command.*;

@Controller
public class RegistrationEmailController implements TelegramController {
    private final RegistrationService registrationService;
    private final TelegramService telegramService;

    @Autowired
    public RegistrationEmailController(RegistrationService registrationService,
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
            return (update.getMessage().hasText() && WaitingEmail.getInstance().getWaitingEmailText());

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
            waitingEmail(update.getCallbackQuery().getMessage().getChatId());
        }
        else if (update.hasMessage() && update.getMessage().hasText()) {
            setEmail(update.getMessage().getChatId(), update.getMessage().getText());
        }
    }

    private void sendNextStep(Long chatId) {

    }

    private void waitingEmail(Long chatId) {
        WaitingEmail.getInstance().setWaitingEmailText(true);
        telegramService.sendMessage(new SendMessage(chatId.toString(), "Введите Ваш email:"));
    }

    private void setEmail(Long chatId, String address) {
        registrationService.setEmail(chatId, address);
        WaitingEmail.getInstance().setWaitingEmailText(false);
        sendNextStep(chatId);
    }
}
