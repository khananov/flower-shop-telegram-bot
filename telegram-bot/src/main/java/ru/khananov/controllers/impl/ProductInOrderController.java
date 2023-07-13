package ru.khananov.controllers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.khananov.controllers.TelegramController;
import ru.khananov.models.entities.Product;
import ru.khananov.models.entities.ProductForCart;
import ru.khananov.services.CartService;
import ru.khananov.services.ProductForCartService;
import ru.khananov.services.ProductService;
import ru.khananov.services.TelegramService;

import static ru.khananov.models.domains.Command.MINUS_AMOUNT_COMMAND;
import static ru.khananov.models.domains.Command.PLUS_AMOUNT_COMMAND;

@Controller
public class ProductInOrderController implements TelegramController {
    private final ProductService productService;
    private final ProductForCartService productForCartService;
    private final CartService cartService;

    @Autowired
    public ProductInOrderController(ProductService productService,
                                    ProductForCartService productForCartService,
                                    CartService cartService) {
        this.cartService = cartService;
        this.productService = productService;
        this.productForCartService = productForCartService;
    }


    @Override
    public boolean support(Update update) {
        if (!update.hasCallbackQuery()) return false;

        return (update.getCallbackQuery().getData().equals(MINUS_AMOUNT_COMMAND.getValue()) ||
                update.getCallbackQuery().getData().equals(PLUS_AMOUNT_COMMAND.getValue()));
    }

    @Override
    public void execute(Update update) {
        if (update.getCallbackQuery().getData().equals(MINUS_AMOUNT_COMMAND.getValue()))
            minus(update);
        else if (update.getCallbackQuery().getData().equals(PLUS_AMOUNT_COMMAND.getValue()))
            plus(update);

    }

    public void plus(Update update) {
        int indexOfNewLine = update.getCallbackQuery().getMessage().getText().indexOf('\n');
        String productName = update.getCallbackQuery().getMessage().getText().substring(0, indexOfNewLine);

        Product product = productService.findByName(productName);
        ProductForCart productForCart = productForCartService.findByProductId(product.getId());

        cartService.sendEditMessageProductInOrder(update.getCallbackQuery().getMessage().getChatId(),
                update.getCallbackQuery().getMessage().getMessageId(),
                productForCartService.plusAmount(productForCart));
    }

    public void minus(Update update) {
        int indexOfNewLine = update.getCallbackQuery().getMessage().getText().indexOf('\n');
        String productName = update.getCallbackQuery().getMessage().getText().substring(0, indexOfNewLine);

        Product product = productService.findByName(productName);
        ProductForCart productForCart = productForCartService.findByProductId(product.getId());

        cartService.sendEditMessageProductInOrder(update.getCallbackQuery().getMessage().getChatId(),
                update.getCallbackQuery().getMessage().getMessageId(),
                productForCartService.minusAmount(productForCart));
    }


}
