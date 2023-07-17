package ru.khananov.services.rabbitservices;

import ru.khananov.dto.MailParams;

public interface TelegramConsumerService {
    void consume(MailParams mailParams);

}
