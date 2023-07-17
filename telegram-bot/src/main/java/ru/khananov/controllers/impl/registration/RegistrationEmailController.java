package ru.khananov.controllers.impl.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.khananov.controllers.TelegramController;
import ru.khananov.models.domains.menukeyboard.MyChangeProfileMenuKeyboardMarkup;
import ru.khananov.services.RegistrationService;
import ru.khananov.services.TelegramService;
import ru.khananov.services.TelegramUserService;

import static ru.khananov.models.domains.Command.*;
import static ru.khananov.models.enums.UserStatus.WAITING_EMAIL_INPUT;

@Controller
public class RegistrationEmailController implements TelegramController {
    private final RegistrationService registrationService;
    private final TelegramService telegramService;
    private final TelegramUserService telegramUserService;

    @Autowired
    public RegistrationEmailController(RegistrationService registrationService,
                                       TelegramService telegramService,
                                       TelegramUserService telegramUserService) {
        this.registrationService = registrationService;
        this.telegramService = telegramService;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public boolean support(Update update) {
        if (update.hasCallbackQuery())
            return telegramUserService.getUserStatusByChatId(update.getCallbackQuery().getMessage().getChatId())
                    .equals(WAITING_EMAIL_INPUT);
        if (update.hasMessage() && update.getMessage().hasText())
            return telegramUserService.getUserStatusByChatId(update.getMessage().getChatId())
                    .equals(WAITING_EMAIL_INPUT);

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
        else if (update.hasMessage()) {
            setEmail(update.getMessage().getChatId(), update.getMessage().getText());
        }
    }

    private void sendNextStep(Long chatId) {
        telegramService.sendReplyKeyboard(MyChangeProfileMenuKeyboardMarkup.getChangeProfileMenuReplyKeyboardMarkup(),
                "Информация успешно сохранена!", chatId);
    }

    private void waitingEmail(Long chatId) {
        telegramService.sendMessage(new SendMessage(chatId.toString(), "Введите Ваш email:"));
    }

    private void setEmail(Long chatId, String address) {
        registrationService.setUserInfo(chatId, address);
        sendNextStep(chatId);
    }
}
