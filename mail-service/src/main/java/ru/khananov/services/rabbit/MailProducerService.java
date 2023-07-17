package ru.khananov.services.rabbit;

import ru.khananov.dto.MailParams;

public interface MailProducerService {
    void produceMailParam(String rabbitQueue, MailParams mailParams);
}
