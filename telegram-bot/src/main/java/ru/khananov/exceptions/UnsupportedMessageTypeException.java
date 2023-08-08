package ru.khananov.exceptions;

public class UnsupportedMessageTypeException extends RuntimeException {
    public UnsupportedMessageTypeException(String message) {
        super(message);
    }
}