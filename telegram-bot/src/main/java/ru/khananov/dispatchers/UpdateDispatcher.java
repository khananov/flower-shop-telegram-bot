package ru.khananov.dispatchers;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.khananov.services.TelegramService;

@Component
@Log4j2
public class UpdateDispatcher {
    private final TelegramService telegramService;

    @Autowired
    public UpdateDispatcher(TelegramService telegramService) {
        this.telegramService = telegramService;
    }

    public void processUpdate(Update update) {
        if (update == null) {
            log.error("Received update is null");
            return;
        }

        if (update.hasMessage()) {
            SendMessage message = new SendMessage();
            message.setChatId(update.getMessage().getChatId());
            message.setText("Hello");
            telegramService.sendMessage(message);
        } else {
            log.error("Unsupported message type is received" + update);
        }
    }
}
