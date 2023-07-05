package ru.khananov.commands;

import java.io.Serializable;

public enum Commands implements Serializable {
    START_COMMAND("/start"),
    CART_COMMAND("Корзина"),
    CATALOG_COMMAND("Каталог"),
    PROFILE_COMMAND("Профиль"),
    NEXT_STEP_COMMAND("Дальше"),
    PREVIOUS_STEP_COMMAND("Назад");

    private final String value;

    Commands(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
