package ru.khananov.services.rabbitservices;

import ru.khananov.dto.MailParamsDto;

public interface TelegramProducerService {
    void produceMail(String rabbitQueue, MailParamsDto mailParamsDto);
}