package ru.khananov.exceptions;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long orderId) {
        super("Order id - " + orderId + " not found");
    }
}
