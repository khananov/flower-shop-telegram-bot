package ru.khananov.services.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.khananov.models.domains.MyProductSendPhoto;
import ru.khananov.models.entities.Product;
import ru.khananov.repositories.ProductRepository;
import ru.khananov.services.ProductService;
import ru.khananov.services.TelegramService;

import java.text.DecimalFormat;
import java.util.List;

@Log4j2
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final TelegramService telegramService;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, TelegramService telegramService) {
        this.productRepository = productRepository;
        this.telegramService = telegramService;
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> findAllByCategoryName(String name) {
        return productRepository.findAllByCategoryName(name);
    }

    @Override
    public String findPriceProduct(Product product) {
        return new DecimalFormat("#0.00").format(convertToRub(product));
    }

    @Override
    public void sendProductsByCategory(Update update) {
        int skipEmojiIndex = 3;
        String categoryName = update.getMessage().getText().substring(skipEmojiIndex);
        List<Product> products = findAllByCategoryName(categoryName);

        products.forEach(product -> telegramService.sendPhoto(new MyProductSendPhoto(
                update.getMessage().getChatId(), product, findPriceProduct(product))
                .getSendPhoto()));
    }

    @Override
    public Product findByName(String name) {
        return productRepository.findByName(name);
    }

    private Double convertToRub(Product product) {
        return  (double) (product.getPrice() / 100);
    }
}
