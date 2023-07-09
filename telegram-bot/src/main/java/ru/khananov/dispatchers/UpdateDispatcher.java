package ru.khananov.dispatchers;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.khananov.controllers.TelegramController;
import ru.khananov.controllers.impl.CartController;
import ru.khananov.controllers.impl.CatalogController;
import ru.khananov.controllers.impl.CategoryController;
import ru.khananov.controllers.impl.StartController;
import ru.khananov.exceptions.UnsupportedMessageTypeException;

import java.util.Arrays;
import java.util.List;

@Log4j2
@Component
public class UpdateDispatcher {
    private final StartController startController;
    private final CatalogController catalogController;
    private final CategoryController categoryController;
    private final CartController cartController;

    @Autowired
    public UpdateDispatcher(StartController startController,
                            CatalogController catalogController,
                            CategoryController categoryController,
                            CartController cartController) {
        this.startController = startController;
        this.catalogController = catalogController;
        this.categoryController = categoryController;
        this.cartController = cartController;
    }

    private List<TelegramController> getControllers() {
        return Arrays.asList(
                startController,
                catalogController,
                categoryController);
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
        getControllers().stream()
                .filter(controller -> controller.support(update.getMessage().getText()))
                .findFirst()
                .ifPresent(controller -> controller.execute(update));
    }

    private void distributeCallbackQuery(Update update) {

    }
}
