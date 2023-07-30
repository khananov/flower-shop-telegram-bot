package ru.khananov.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super("Username - " + username + " not found.");
    }

    public UserNotFoundException(Long chatId) {
        super("Username with chat id - " + chatId + " not found.");
    }
}
