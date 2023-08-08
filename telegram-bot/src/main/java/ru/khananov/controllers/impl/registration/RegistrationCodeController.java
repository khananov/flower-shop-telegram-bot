package ru.khananov.controllers.impl.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.khananov.controllers.TelegramController;
import ru.khananov.models.domains.TemporalCodeCache;
import ru.khananov.models.domains.menukeyboard.MyGeneralMenuKeyboardMarkup;
import ru.khananov.services.RegistrationService;
import ru.khananov.services.TelegramService;
import ru.khananov.services.TelegramUserService;

import static ru.khananov.models.domains.Command.CANCEL_CODE_COMMAND;
import static ru.khananov.models.enums.UserStatus.REGISTERED;
import static ru.khananov.models.enums.UserStatus.WAITING_CODE_INPUT;

@Controller
public class RegistrationCodeController implements TelegramController {
    private final TelegramUserService telegramUserService;
    private final RegistrationService registrationService;
    private final TelegramService telegramService;

    @Autowired
    public RegistrationCodeController(TelegramUserService telegramUserService,
                                      RegistrationService registrationService,
                                      TelegramService telegramService) {
        this.telegramUserService = telegramUserService;
        this.registrationService = registrationService;
        this.telegramService = telegramService;
    }

    @Override
    public boolean support(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            return telegramUserService.getUserStatusByChatId(update.getMessage().getChatId())
                    .equals(WAITING_CODE_INPUT);
        }

        return false;
    }

    @Override
    public void execute(Update update) {
        if (update.getMessage().getText().equals(CANCEL_CODE_COMMAND.getValue()))
            cancelInputCode(update.getMessage().getChatId());
        else
            checkInputCode(update.getMessage().getChatId(), update.getMessage().getText());

    }

    private void checkInputCode(Long chatId, String inputCode) {
        registrationService.codeCheck(chatId, inputCode);
    }

    private void cancelInputCode(Long chatId) {
        TemporalCodeCache.getInstance().deleteCodeByChatId(chatId);

        telegramUserService.updateUserStatus(chatId, REGISTERED);
        telegramService.sendMessage(new SendMessage(chatId.toString(), "Подтверждение почты отменено!"));
        telegramService.sendReplyKeyboard(MyGeneralMenuKeyboardMarkup.getGeneralMenuReplyKeyboardMarkup(),
                "Выберите действие",
                chatId);
    }
}