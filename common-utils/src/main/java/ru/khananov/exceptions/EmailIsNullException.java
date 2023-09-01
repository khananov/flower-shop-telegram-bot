package ru.khananov.exceptions;

public class EmailIsNullException extends RuntimeException {
    public EmailIsNullException(String chatId) {
        super("Sending temp code error, email is null. Chat id - " + chatId);
    }
}
