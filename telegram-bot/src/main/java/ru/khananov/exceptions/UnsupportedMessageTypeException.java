package ru.khananov.exceptions;

public class UnsupportedMessageTypeException extends RuntimeException {
    public UnsupportedMessageTypeException() {

    }

    public UnsupportedMessageTypeException(String message) {
        super(message);
    }

    public UnsupportedMessageTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
