package ru.khananov.models.enums;

import java.io.Serializable;

public enum OrderStatus implements Serializable {

    NEW("New"),
    PAID("Paid"),
    CANCELED("Canceled"),
    CLOSED("Closed");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
