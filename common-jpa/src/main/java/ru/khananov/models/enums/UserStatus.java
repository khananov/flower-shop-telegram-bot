package ru.khananov.models.enums;

public enum UserStatus {
    NEW("New"),
    WAITING_NAME_INPUT("Waiting_name_input"),
    WAITING_ADDRESS_INPUT("Waiting_address_input"),
    WAITING_EMAIL_INPUT("Waiting_email_input"),
    WAITING_CODE_INPUT("Waiting_code_input"),
    REGISTERED("Registered"),
    CONFIRMED("Confirmed");

    private final String value;

    UserStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
