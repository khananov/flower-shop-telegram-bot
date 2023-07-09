package ru.khananov.models.domains;

import java.io.Serializable;

public enum Command implements Serializable {
    START_COMMAND("/start"),
    CART_COMMAND("\uD83D\uDECD Корзина"),
    CATALOG_COMMAND("\uD83D\uDCD6 Каталог"),
    PROFILE_COMMAND("\uD83D\uDC64 Профиль"),

    WILD_FLOWERS_COMMAND("Полевые"),
    GARDEN_FLOWERS_COMMAND("Садовые"),

    BUY_COMMAND("\uD83D\uDCB3 Купить"),

    NEXT_STEP_COMMAND("Дальше"),
    PREVIOUS_STEP_COMMAND("Назад"),

    MAIN_MENU_COMMAND("Главная");

    private final String value;

    Command(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
