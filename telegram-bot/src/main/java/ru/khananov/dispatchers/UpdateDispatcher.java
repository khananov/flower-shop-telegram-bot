package ru.khananov.dispatchers;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.khananov.controllers.CatalogController;
import ru.khananov.controllers.StartController;
import ru.khananov.exceptions.UnsupportedMessageTypeException;

import static ru.khananov.commands.Commands.*;

@Log4j2
@Component
public class UpdateDispatcher {
    private final StartController startController;
    private final CatalogController catalogController;

    @Autowired
    public UpdateDispatcher(StartController startController, CatalogController catalogController) {
        this.startController = startController;
        this.catalogController = catalogController;
    }

    public void processUpdate(Update update) {
        if (update == null) {
            log.error("Received update is null");
            return;
        }

        if (update.hasMessage())
            distributeMessageByCommand(update);
        else if (update.hasCallbackQuery())
            distributeCallbackQuery(update);
        else
            log.error(new UnsupportedMessageTypeException("Неподдерживаемый тип сообщения"));
    }

    private void distributeMessageByCommand(Update update) {
        Message message = update.getMessage();

        if (message.getText().equals(START_COMMAND.getValue()))
            startController.startMethod(update);
        else if (message.getText().equals(CATALOG_COMMAND.getValue()))
            catalogController.sendCategories(update);
//        else if (message.getText().equals(CATALOG_COMMAND.getValue()))
//            catalogController
//        else if (message.getText().equals(CATALOG_COMMAND.getValue()))
//            catalogController
    }

    private void distributeCallbackQuery(Update update) {
        catalogController.sendProductsByCategory(update, update.getCallbackQuery());
    }
}
