package ru.khananov.controllers.impl;

import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.khananov.controllers.TelegramController;

@Controller
public class CartController implements TelegramController {

    @Override
    public boolean support(Update update) {
        if (!update.hasMessage()) return false;
        return false;
    }

    @Override
    public void execute(Update update) {

    }
}
