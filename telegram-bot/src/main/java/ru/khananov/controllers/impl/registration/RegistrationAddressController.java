package ru.khananov.controllers.impl.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.khananov.controllers.TelegramController;
import ru.khananov.models.domains.inlinekeyboard.MyRegistrationInlineKeyboard;
import ru.khananov.services.RegistrationService;
import ru.khananov.services.TelegramService;
import ru.khananov.services.TelegramUserService;

import static ru.khananov.models.domains.Command.CONFIRM_COMMAND;
import static ru.khananov.models.domains.Command.REJECT_COMMAND;
import static ru.khananov.models.enums.UserStatus.WAITING_ADDRESS_INPUT;

@Controller
public class RegistrationAddressController implements TelegramController {
    private final RegistrationService registrationService;
    private final TelegramService telegramService;
    private final TelegramUserService telegramUserService;

    @Autowired
    public RegistrationAddressController(RegistrationService registrationService,
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
                    .equals(WAITING_ADDRESS_INPUT);
        if (update.hasMessage() && update.getMessage().hasText())
            return telegramUserService.getUserStatusByChatId(update.getMessage().getChatId())
                    .equals(WAITING_ADDRESS_INPUT);

        return false;
    }

    @Override
    public void execute(Update update) {
        if (update.hasCallbackQuery() &&
                update.getCallbackQuery().getData().equals(CONFIRM_COMMAND.getValue())) {
            sendNextStep(update.getCallbackQuery().getMessage().getChatId(),
                    update.getCallbackQuery().getMessage().getMessageId());
        }
        else if (update.hasCallbackQuery() &&
                update.getCallbackQuery().getData().equals(REJECT_COMMAND.getValue())) {
            waitingAddress(update.getCallbackQuery().getMessage().getChatId(),
                    update.getCallbackQuery().getMessage().getMessageId());
        }
        else if (update.hasMessage()) {
            setAddress(update.getMessage().getChatId(), update.getMessage().getText());
        }
    }

    private void sendNextStep(Long chatId, Integer messageId) {
        telegramService.deleteMessage(new DeleteMessage(chatId.toString(), messageId));
        registrationService.sendEmailInlineKeyboard(chatId,
                MyRegistrationInlineKeyboard.getRegistrationKeyboardMarkup());
    }

    private void waitingAddress(Long chatId, Integer messageId) {
        telegramService.deleteMessage(new DeleteMessage(chatId.toString(), messageId));
        telegramService.sendMessage(new SendMessage(chatId.toString(), "Введите Ваш адрес:"));
    }

    private void setAddress(Long chatId, String address) {
        registrationService.setUserInfo(chatId, address);
        registrationService.sendEmailInlineKeyboard(chatId,
                MyRegistrationInlineKeyboard.getRegistrationKeyboardMarkup());    }
}