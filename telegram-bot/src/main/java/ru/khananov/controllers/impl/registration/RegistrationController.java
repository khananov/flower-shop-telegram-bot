package ru.khananov.controllers.impl.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.khananov.controllers.TelegramController;
import ru.khananov.models.domains.MyRegistrationInlineKeyboard;
import ru.khananov.models.domains.registration.WaitingName;
import ru.khananov.services.RegistrationService;
import ru.khananov.services.TelegramService;

import static ru.khananov.models.domains.Command.REGISTRATION_COMMAND;

@Controller
public class RegistrationController implements TelegramController {
    private final RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @Override
    public boolean support(Update update) {
        if (!update.hasMessage()) return false;

        return update.getMessage().getText().equals(REGISTRATION_COMMAND.getValue());
    }

    @Override
    public void execute(Update update) {
        registration(update.getMessage().getChatId());
    }

    private void registration(Long chatId) {
        registrationService.sendNameInlineKeyboard(chatId,
                MyRegistrationInlineKeyboard.getRegistrationKeyboardMarkup());
    }
}
