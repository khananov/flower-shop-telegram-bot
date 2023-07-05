package ru.khananov.commands;

import java.io.Serializable;

public enum Commands implements Serializable {
    START_COMMAND("/start"),
    CART_COMMAND("/cart"),
    CATALOG_COMMAND("/catalog"),
    PROFILE_COMMAND("/profile"),
    NEXT_STEP_COMMAND("/next"),
    PREVIOUS_STEP_COMMAND("/previous");

    private final String value;

    Commands(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
