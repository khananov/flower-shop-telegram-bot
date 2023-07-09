package ru.khananov.models.domains;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.khananov.models.entities.Product;
import ru.khananov.models.entities.ProductForCart;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton.builder;
import static ru.khananov.models.domains.Command.CART_COMMAND;
import static ru.khananov.models.domains.Command.CATALOG_COMMAND;

public class MyInlineQueryResult {

}
