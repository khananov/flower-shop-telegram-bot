package ru.khananov.exceptions;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("Product id - " + id + " not found");
    }

    public ProductNotFoundException(String name) {
        super("Product with name - " + name + " not found");
    }
}
