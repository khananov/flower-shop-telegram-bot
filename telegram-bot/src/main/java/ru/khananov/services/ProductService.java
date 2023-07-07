package ru.khananov.services;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.khananov.models.entities.Product;

import java.util.List;

public interface ProductService {
    List<Product> findAllByCategoryId(CallbackQuery callbackQuery);
}
