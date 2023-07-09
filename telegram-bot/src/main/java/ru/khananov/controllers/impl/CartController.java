package ru.khananov.controllers.impl;

import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.khananov.controllers.TelegramController;

@Controller
public class CartController implements TelegramController {

    @Override
    public boolean support(String command) {
        return false;
    }

    @Override
    public void execute(Update update) {

    }
}
