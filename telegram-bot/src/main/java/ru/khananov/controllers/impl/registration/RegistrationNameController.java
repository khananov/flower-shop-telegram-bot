package ru.khananov.controllers.impl.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.khananov.controllers.TelegramController;
import ru.khananov.models.domains.inlinekeyboard.MyRegistrationInlineKeyboard;
import ru.khananov.services.RegistrationService;
import ru.khananov.services.TelegramService;
import ru.khananov.services.TelegramUserService;

import static ru.khananov.models.domains.Command.CONFIRM_COMMAND;
import static ru.khananov.models.domains.Command.REJECT_COMMAND;
import static ru.khananov.models.enums.UserStatus.WAITING_NAME_INPUT;

@Controller
public class RegistrationNameController implements TelegramController {
    private final TelegramService telegramService;
    private final RegistrationService registrationService;
    private final TelegramUserService telegramUserService;

    @Autowired
    public RegistrationNameController(TelegramService telegramService,
                                      RegistrationService registrationService,
                                      TelegramUserService telegramUserService) {
        this.telegramService = telegramService;
        this.registrationService = registrationService;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public boolean support(Update update) {
        if (update.hasCallbackQuery())
            return telegramUserService.getUserStatusByChatId(update.getCallbackQuery().getMessage().getChatId())
                .equals(WAITING_NAME_INPUT);
        if (update.hasMessage() && update.getMessage().hasText())
            return telegramUserService.getUserStatusByChatId(update.getMessage().getChatId())
                    .equals(WAITING_NAME_INPUT);

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
        else if (update.hasMessage()) {
            setName(update.getMessage().getChatId(), update.getMessage().getText());
        }
    }

    private void sendNextStep(Long chatId) {
        registrationService.sendAddressInlineKeyboard(chatId,
                MyRegistrationInlineKeyboard.getRegistrationKeyboardMarkup());
    }

    private void waitingName(Long chatId) {
        telegramService.sendMessage(new SendMessage(chatId.toString(), "Введите Ваше имя:"));
    }

    private void setName(Long chatId, String address) {
        registrationService.setUserInfo(chatId, address);
        sendNextStep(chatId);
    }
}
