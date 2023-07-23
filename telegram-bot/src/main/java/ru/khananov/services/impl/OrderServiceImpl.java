package ru.khananov.services.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.invoices.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;
import org.telegram.telegrambots.meta.api.objects.payments.SuccessfulPayment;
import ru.khananov.models.domains.inlinekeyboard.MyCancelOrderInlineKeyboardMarkup;
import ru.khananov.models.entities.Order;
import ru.khananov.models.entities.ProductForCart;
import ru.khananov.models.entities.TelegramUser;
import ru.khananov.repositories.OrderRepository;
import ru.khananov.services.OrderService;
import ru.khananov.services.TelegramService;
import ru.khananov.services.TelegramUserService;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static ru.khananov.models.enums.OrderStatus.*;

@Log4j2
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final TelegramUserService telegramUserService;
    private final TelegramService telegramService;

    @Value("${payment.token}")
    private String paymentToken;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            TelegramUserService telegramUserService,
                            TelegramService telegramService) {
        this.orderRepository = orderRepository;
        this.telegramUserService = telegramUserService;
        this.telegramService = telegramService;
    }

    @Override
    public Order findLastOrderByChatId(Long chatId) {
        TelegramUser telegramUser = telegramUserService.findByChatId(chatId);

        List<Order> orders = orderRepository.findAllByTelegramUserId(telegramUser.getId());
        Order lastOrder = orders.stream()
                .filter(o -> o.getOrderStatus() == NEW)
                .findFirst()
                .orElse(null);

        if (lastOrder == null)
            lastOrder = createOrder(telegramUser);

        return orderRepository.save(lastOrder);
    }

    @Override
    public void sendAllOrdersByChatId(Long chatId) {
        TelegramUser user = telegramUserService.findByChatId(chatId);
        List<Order> orders = orderRepository.findAllByTelegramUserId(user.getId());
        orders = orders.stream().filter(order -> order.getOrderStatus() != NEW).toList();

        if (orders.isEmpty())
            telegramService.sendMessage(new SendMessage(chatId.toString(), "У вас нет оплаченных заказов"));
        else {
            telegramService.sendMessage(new SendMessage(chatId.toString(), "Список заказов:\n"));

            for (Order order : orders) {
                if (order.getOrderStatus().equals(CANCELED))
                    telegramService.sendMessage(new SendMessage(
                            chatId.toString(), mapOrderToString(order)));
                else
                    telegramService.sendInlineKeyboard(
                            MyCancelOrderInlineKeyboardMarkup.getCancelOrderKeyboardMarkup(),
                            mapOrderToString(order), chatId);
            }
        }
    }

    @Override
    public void payForOrder(Long chatId) {
        Order order = findLastOrderByChatId(chatId);

        if (order.getProductsForCart().isEmpty()) {
            telegramService.sendMessage(new SendMessage(chatId.toString(),
                    "Ваша корзина пуста"));
        } else {
            telegramService.sendMessage(new SendMessage(chatId.toString(),
                    """
                            Внимание!
                            Сумма тестового заказа должны быть менее 1000 рублей.
                            Для оплаты используйте данные тестовой карты
                                                        
                            Номер карты: 1111 1111 1111 1026
                            Действует до: 12/24 CVV: 000
                            """));

            telegramService.sendInvoiceMessage(createSendInvoice(chatId, order));
        }
    }

    private Order createOrder(TelegramUser telegramUser) {
        return Order.builder()
                .telegramUser(telegramUser)
                .orderStatus(NEW)
                .productsForCart(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private String mapOrderToString(Order order) {
        StringBuilder message = new StringBuilder();

        message.append("Заказ №").append(order.getId()).append("\n");

        order.getProductsForCart()
                .forEach(product -> message.append(product.getProduct().getName())
                        .append(", количество - ").append(product.getAmount())
                        .append(" шт.").append("\n"));

        message.append("\n").append("Дата создания заказа: ")
                .append(order.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))).append("\n")
                .append("Сумма заказа: ")
                .append(new DecimalFormat("#0.00").format(calculateOrderSum(order) / 100))
                .append("\n").append("Статус заказа: ").append(order.getOrderStatus());

        return message.toString();
    }

    @Override
    public void cancelOrder(Message message) {
        int startIndexOrderId = message.getText().indexOf("№") + 1;
        int endIndexOrderId = message.getText().indexOf("\n");
        String orderId = message.getText().substring(startIndexOrderId, endIndexOrderId);

        Order canceledOrder = orderRepository.findById(Long.valueOf(orderId)).orElse(null);

        if (canceledOrder != null) {
            canceledOrder.setOrderStatus(CANCELED);
            orderRepository.save(canceledOrder);
            telegramService.sendMessage(new SendMessage(
                    message.getChatId().toString(), "Заказ №" + orderId + " отменен"));
        }
    }

    @Override
    public void updateOrderStatusToPaid(SuccessfulPayment successfulPayment) {
        Order order = orderRepository.findById(Long.valueOf(successfulPayment.getInvoicePayload()))
                .orElse(null);

        if (order != null) {
            order.setOrderStatus(PAID);
            orderRepository.save(order);
        }
    }

    private Long calculateOrderSum(Order order) {
        List<ProductForCart> products = order.getProductsForCart();

        return (long) products.stream()
                .mapToInt(product -> Math.toIntExact(product.getPrice() * product.getAmount()))
                .sum();
    }

    private SendInvoice createSendInvoice(Long chatId, Order order) {
        SendInvoice invoice = new SendInvoice();
        invoice.setChatId(chatId);
        invoice.setTitle("Заказ №" + order.getId());
        invoice.setDescription("Тестовая оплата");
        invoice.setPayload(order.getId().toString());
        invoice.setProviderToken(paymentToken);
        invoice.setCurrency("RUB");
        invoice.setPrices(createLabelPrice(order));

        return invoice;
    }

    private List<LabeledPrice> createLabelPrice(Order order) {
        List<LabeledPrice> labeledPrices = new ArrayList<>();
        for (ProductForCart product : order.getProductsForCart()) {
            LabeledPrice labeledPrice = new LabeledPrice(product.getProduct().getName(),
                    (int) (product.getAmount() * product.getPrice()));
            labeledPrices.add(labeledPrice);
        }
        return labeledPrices;
    }
}
