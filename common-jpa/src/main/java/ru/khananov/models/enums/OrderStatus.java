package ru.khananov.models.enums;

public enum OrderStatus {
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
