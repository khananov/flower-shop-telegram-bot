package ru.khananov.exceptions;

public class ProductForCartNotFoundException extends RuntimeException {
    public ProductForCartNotFoundException(Long id) {
        super("Product for cart id - " + id + " not found");
    }
}