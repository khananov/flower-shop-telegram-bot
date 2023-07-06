package ru.khananov.dispatchers;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.khananov.controllers.StartController;
import ru.khananov.exceptions.UnsupportedMessageTypeException;

@Log4j2
@Component
public class UpdateDispatcher {
    private final StartController startController;

    @Autowired
    public UpdateDispatcher(StartController startController) {
        this.startController = startController;
    }

    public void processUpdate(Update update) {
        if (update == null) {
            log.error("Received update is null");
            return;
        }

        if (update.hasMessage()) {
            distributeMessageByCommand(update);
        } else {
            log.error(new UnsupportedMessageTypeException("Неподдерживаемый тип сообщения"));
        }
    }

    private void distributeMessageByCommand(Update update) {
        Message message = update.getMessage();

        switch (message.getText()) {
            case "/start" -> startController.startMethod(update);
//            case CATALOG_COMMAND -> startController.send(update);
        }
    }
}
