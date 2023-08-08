package ru.khananov.services.impl.rabbitservicesimpl;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.khananov.dto.MailParamsDto;
import ru.khananov.services.rabbitservices.TelegramProducerService;

@Service
public class TelegramProducerServiceImpl implements TelegramProducerService {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public TelegramProducerServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void produceMail(String rabbitQueue, MailParamsDto mailParamsDto) {
        rabbitTemplate.convertAndSend(rabbitQueue, mailParamsDto);
    }
}