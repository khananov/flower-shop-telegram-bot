package ru.khananov.services.rabbit;

import ru.khananov.dto.MailParams;

public interface MailConsumerService {
    void consumerMailParams(MailParams mailParams);
}
