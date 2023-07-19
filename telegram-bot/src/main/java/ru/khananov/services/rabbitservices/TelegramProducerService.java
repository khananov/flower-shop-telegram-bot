package ru.khananov.services.rabbitservices;

import ru.khananov.dto.MailParamsDto;
import ru.khananov.dto.PurchaseParamsDto;

public interface TelegramProducerService {
    void produceMail(String rabbitQueue, MailParamsDto mailParamsDto);

    void producePurchase(String rabbitQueue, PurchaseParamsDto purchaseParamsDto);
}
