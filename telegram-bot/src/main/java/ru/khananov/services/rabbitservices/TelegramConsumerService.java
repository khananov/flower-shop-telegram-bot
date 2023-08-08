package ru.khananov.services.rabbitservices;

import ru.khananov.dto.MailParamsDto;

public interface TelegramConsumerService {
    void consume(MailParamsDto mailParamsDto);
}
