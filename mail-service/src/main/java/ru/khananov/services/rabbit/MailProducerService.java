package ru.khananov.services.rabbit;

import ru.khananov.dto.MailParamsDto;

public interface MailProducerService {
    void produceMailParam(String rabbitQueue, MailParamsDto mailParamsDto);
}
