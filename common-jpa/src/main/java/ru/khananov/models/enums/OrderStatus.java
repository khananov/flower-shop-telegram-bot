package ru.khananov.models.enums;

import java.io.Serializable;

public enum OrderStatus implements Serializable {

    NEW("Новый"),
    PAID("Оплачен"),
    CANCELED("Отменён"),
    DELIVERED("Доставлен");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
