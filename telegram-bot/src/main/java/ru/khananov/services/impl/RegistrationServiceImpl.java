package ru.khananov.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ru.khananov.models.domains.TemporalCodeCache;
import ru.khananov.models.domains.inlinekeyboard.MyRegistrationInlineKeyboard;
import ru.khananov.models.domains.menukeyboard.MyChangeProfileMenuKeyboardMarkup;
import ru.khananov.models.domains.menukeyboard.MyGeneralMenuKeyboardMarkup;
import ru.khananov.models.entities.TelegramUser;
import ru.khananov.repositories.TelegramUserRepository;
import ru.khananov.services.RegistrationService;
import ru.khananov.services.TelegramService;
import ru.khananov.services.TelegramUserService;
import ru.khananov.services.rabbitservices.TelegramProducerService;

import static ru.khananov.models.enums.UserStatus.*;

@Service
public class RegistrationServiceImpl implements RegistrationService {
    private final TelegramUserRepository telegramUserRepository;
    private final TelegramService telegramService;
    private final TelegramProducerService telegramProducerService;
    private final TelegramUserService telegramUserService;

    @Autowired
    public RegistrationServiceImpl(TelegramUserRepository telegramUserRepository,
                                   TelegramService telegramService,
                                   TelegramProducerService telegramProducerService,
                                   TelegramUserService telegramUserService) {
        this.telegramUserRepository = telegramUserRepository;
        this.telegramService = telegramService;
        this.telegramProducerService = telegramProducerService;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public void sendNameInlineKeyboard(Long chatId, InlineKeyboardMarkup keyboardMarkup) {
        TelegramUser user = telegramUserService.findByChatId(chatId);

        if (user.getFirstName() == null)
            telegramService.sendMessage(new SendMessage(chatId.toString(), "Введите Ваше имя:"));
        else
            telegramService.sendInlineKeyboard(MyRegistrationInlineKeyboard.getRegistrationKeyboardMarkup(),
                    "Ваше имя - " + user.getFirstName() + "?", chatId);

        user.setUserStatus(WAITING_NAME_INPUT);
        telegramUserRepository.save(user);
    }

    @Override
    public void sendAddressInlineKeyboard(Long chatId, InlineKeyboardMarkup keyboardMarkup) {
        TelegramUser user = telegramUserService.findByChatId(chatId);

        if (user.getAddress() == null)
            telegramService.sendMessage(new SendMessage(chatId.toString(), "Введите Ваш адрес:"));
        else
            telegramService.sendInlineKeyboard(MyRegistrationInlineKeyboard.getRegistrationKeyboardMarkup(),
                    "Ваш адрес - " + user.getAddress() + "?", chatId);

        user.setUserStatus(WAITING_ADDRESS_INPUT);
        telegramUserRepository.save(user);
    }

    @Override
    public void sendEmailInlineKeyboard(Long chatId, InlineKeyboardMarkup keyboardMarkup) {
        TelegramUser user = telegramUserService.findByChatId(chatId);

        if (user.getEmail() == null)
            telegramService.sendMessage(new SendMessage(chatId.toString(), "Введите Ваш email:"));
        else
            telegramService.sendInlineKeyboard(keyboardMarkup,
                    "Ваш email - " + user.getEmail() + "?", chatId);

        user.setUserStatus(WAITING_EMAIL_INPUT);
        telegramUserRepository.save(user);
    }

    @Override
    public void sendCodeInlineKeyboard(Long chatId, ReplyKeyboardMarkup keyboardMarkup) {
        TelegramUser user = telegramUserService.findByChatId(chatId);

        if (user.getUserStatus().equals(CONFIRMED))
            telegramService.sendReplyKeyboard(
                    MyChangeProfileMenuKeyboardMarkup.getChangeProfileMenuReplyKeyboardMarkup(),
                    "Вы уже подтвердили почту", chatId);
        else {
            telegramService.sendReplyKeyboard(keyboardMarkup,
                    "На email - " + user.getEmail() + " отправлено письмо с кодом.\n" +
                            "Отправьте его сообщением боту для подтверждения почты.", chatId);

            telegramProducerService.produceMail(
                    "mail_verification_queue",
                    telegramUserService.mapUserToMailParamsDto(user));

            user.setUserStatus(WAITING_CODE_INPUT);
            telegramUserRepository.save(user);
        }
    }

    @Override
    public void setUserInfo(Long chatId, String info) {
        TelegramUser user = telegramUserService.findByChatId(chatId);

        if (user.getUserStatus().equals(WAITING_NAME_INPUT))
            user.setFirstName(info);
        else if (user.getUserStatus().equals(WAITING_ADDRESS_INPUT))
            user.setAddress(info);
        else if (user.getUserStatus().equals(WAITING_EMAIL_INPUT)) {
            if (info != null)
                user.setEmail(info);
            user.setUserStatus(REGISTERED);
        }

        telegramUserRepository.save(user);
    }

    @Override
    public void codeCheck(Long chatId, String inputCode) {
        TelegramUser user = telegramUserService.findByChatId(chatId);

        if (inputCode.equals(TemporalCodeCache.getInstance().getCodeByChatId(chatId))) {
            TemporalCodeCache.getInstance().deleteCodeByChatId(chatId);

            user.setUserStatus(CONFIRMED);
            telegramUserRepository.save(user);

            telegramService.sendReplyKeyboard(MyGeneralMenuKeyboardMarkup.getGeneralMenuReplyKeyboardMarkup(),
                    "Почта подтверждена",
                    chatId);
        } else
            telegramService.sendMessage(new SendMessage(chatId.toString(),
                    "Неверный код. Попробуйте еще раз"));
    }
}