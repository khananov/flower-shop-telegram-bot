package ru.khananov.services.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ru.khananov.models.domains.menukeyboard.MyChangeProfileMenuKeyboardMarkup;
import ru.khananov.models.domains.menukeyboard.MyRegistrationProfileMenuKeyboardMarkup;
import ru.khananov.models.entities.TelegramUser;
import ru.khananov.repositories.TelegramUserRepository;
import ru.khananov.services.TelegramService;
import ru.khananov.services.TelegramUserService;

@Log4j2
@Service
public class TelegramUserServiceImpl implements TelegramUserService {
    private final TelegramService telegramService;
    private final TelegramUserRepository telegramUserRepository;

    @Autowired
    public TelegramUserServiceImpl(TelegramService telegramService,
                                   TelegramUserRepository telegramUserRepository) {
        this.telegramService = telegramService;
        this.telegramUserRepository = telegramUserRepository;
    }

    @Override
    public TelegramUser findByChatId(Long chatId) {
        return telegramUserRepository.findByChatId(chatId);
    }

    @Override
    public TelegramUser registerUser(Message message) {
        TelegramUser telegramUser = findByChatId(message.getChatId());

        if (telegramUser == null) {
            return telegramUserRepository.save(buildTelegramUser(message));
        } else {
            return setActive(telegramUser);
        }
    }

    @Override
    public void sendProfileMessage(Long chatId) {
        TelegramUser user = telegramUserRepository.findByChatId(chatId);
        if (user.getEmail() == null)
            telegramService.sendMessage(buildProfileMessage(chatId,
                    MyRegistrationProfileMenuKeyboardMarkup.getProfileMenuReplyKeyboardMarkup(),
                    user));
        else
            telegramService.sendMessage(buildProfileMessage(chatId,
                    MyChangeProfileMenuKeyboardMarkup.getChangeProfileMenuReplyKeyboardMarkup(),
                    user));
    }

    @Override
    public void deleteProfile(Long chatId) {
        TelegramUser user = telegramUserRepository.findByChatId(chatId);
        telegramUserRepository.delete(user);
    }

    private SendMessage buildProfileMessage(Long chatId, ReplyKeyboardMarkup keyboardMarkup,
                                            TelegramUser user) {
        String firstName = user.getFirstName() != null ? user.getFirstName() : "";
        String lastName = user.getLastName() != null ? user.getLastName() : "";
        String username = user.getUsername() != null ? user.getUsername() : "";
        String address = user.getAddress() != null ? user.getAddress() : "";
        String email = user.getEmail() != null ? user.getEmail() : "";

        return SendMessage.builder()
                .chatId(chatId)
                .replyMarkup(keyboardMarkup)
                .text("= = = = = = = = = = = = = = = = =\n" +
                        "\n \u2139 Информация о Вас: \n" +
                        "\n \uD83D\uDCAD Имя: " + firstName + " " + lastName +
                        "\n \uD83D\uDD10 Username: " + username +
                        "\n \uD83C\uDF0F Адрес: " + address +
                        "\n \uD83D\uDCEA Email: " + email + "\n" +
                        "\n= = = = = = = = = = = = = = = = =")
                .build();
    }

    private TelegramUser buildTelegramUser(Message message) {
        return TelegramUser.builder().
                chatId(message.getChatId()).
                firstName(message.getChat().getFirstName()).
                lastName(message.getChat().getLastName()).
                username(message.getChat().getUserName()).
                isActive(true).build();
    }

    private TelegramUser setActive(TelegramUser telegramUser) {
        telegramUser.setIsActive(true);
        return telegramUserRepository.save(telegramUser);
    }
}
