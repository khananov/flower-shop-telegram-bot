package ru.khananov.services.rabbit;

import ru.khananov.dto.MailParamsDto;

public interface MailConsumerService {
    void consumerMailParams(MailParamsDto mailParamsDto);
}