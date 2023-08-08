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
import ru.khananov.exceptions.OrderNotFoundException;
import ru.khananov.models.domains.inlinekeyboard.MyCancelOrderInlineKeyboardMarkup;
import ru.khananov.models.entities.Order;
import ru.khananov.models.entities.ProductForCart;
import ru.khananov.models.entities.TelegramUser;
import ru.khananov.repositories.OrderRepository;
import ru.khananov.services.TelegramService;
import ru.khananov.services.TelegramUserService;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.khananov.models.enums.OrderStatus.*;

@Service
@Log4j2
public class OrderServiceImpl implements ru.khananov.services.OrderService {
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

        if (lastOrder == null) {
            lastOrder = createOrder(telegramUser);
            orderRepository.save(lastOrder);
        }

        return lastOrder;
    }

    @Override
    public void sendAllOrdersByChatId(Long chatId) {
        List<Order> orders = getSortedOrdersByChatId(chatId);

        if (orders.isEmpty())
            telegramService.sendMessage(new SendMessage(chatId.toString(), "У вас нет оплаченных заказов"));
        else {
            telegramService.sendMessage(new SendMessage(chatId.toString(), "Список заказов:\n"));

            for (Order order : orders) {
                if (order.getOrderStatus().equals(PAID))
                    telegramService.sendInlineKeyboard(
                            MyCancelOrderInlineKeyboardMarkup.getCancelOrderKeyboardMarkup(),
                            mapOrderToString(order), chatId);
                else
                    telegramService.sendMessage(new SendMessage(
                            chatId.toString(), mapOrderToString(order)));
            }
        }
    }

    @Override
    public void payForOrder(Long chatId) {
        Order order = findLastOrderByChatId(chatId);

        if (order.getProductsForCart().isEmpty())
            telegramService.sendMessage(new SendMessage(chatId.toString(),
                    "Ваша корзина пуста"));
        else {
            telegramService.sendMessage(new SendMessage(chatId.toString(),
                    """
                            Внимание!
                            Сумма тестового заказа должна быть менее 1000 рублей.
                            Для оплаты используйте данные тестовой карты:
                                                        
                            Номер карты: 1111 1111 1111 1026
                            Действует до: 12/24 CVV: 000
                            """));

            telegramService.sendInvoiceMessage(createSendInvoice(chatId, order));
        }
    }

    @Override
    public void cancelOrder(Message message) {
        int startIndexOrderId = message.getText().indexOf("№") + 1;
        int endIndexOrderId = message.getText().indexOf("\n");
        String orderId = message.getText().substring(startIndexOrderId, endIndexOrderId);

        Order canceledOrder = findOrderById(orderId);
        canceledOrder.setOrderStatus(CANCELED);

        orderRepository.save(canceledOrder);
        telegramService.sendMessage(new SendMessage(
                    message.getChatId().toString(), "Заказ №" + orderId + " отменен"));
    }

    @Override
    public void updateOrderStatusToPaid(SuccessfulPayment successfulPayment) {
        Order order = findOrderById(successfulPayment.getInvoicePayload());
        order.setOrderStatus(PAID);
        orderRepository.save(order);
    }

    @Override
    public boolean checkTotalAmountOrder(String textOrderId, Integer totalAmountPurchase) {
        return getTotalOrderAmount(findOrderById(textOrderId)) == totalAmountPurchase;
    }

    private Order findOrderById(String stringOrderId) {
        Long orderId = Long.valueOf(stringOrderId);

        return orderRepository.findById(orderId)
                .orElseGet(() -> {
                    log.error(new OrderNotFoundException(orderId));
                    throw new OrderNotFoundException(orderId);
                });
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
                .append(new DecimalFormat("#0.00").format(getTotalOrderAmount(order) / 100))
                .append("\n").append("Статус заказа: ").append(order.getOrderStatus().getValue());

        return message.toString();
    }

    private int getTotalOrderAmount(Order order) {
        return order.getProductsForCart().stream()
                .mapToInt(product -> (int) (product.getAmount() * product.getPrice()))
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

    private List<Order> getSortedOrdersByChatId(Long chatId) {
        TelegramUser user = telegramUserService.findByChatId(chatId);
        List<Order> orders = orderRepository.findAllByTelegramUserId(user.getId());

        return orders.stream()
                .filter(order -> order.getOrderStatus() != NEW)
                .sorted(Comparator.comparing(Order::getId))
                .collect(Collectors.toList());
    }
}
