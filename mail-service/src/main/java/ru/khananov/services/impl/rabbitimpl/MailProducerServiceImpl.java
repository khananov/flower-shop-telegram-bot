package ru.khananov.services.impl.rabbitimpl;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.khananov.dto.MailParams;
import ru.khananov.services.rabbit.MailProducerService;

@Service
public class MailProducerServiceImpl implements MailProducerService {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MailProducerServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void produceMailParam(String rabbitQueue, MailParams mailParams) {
        rabbitTemplate.convertAndSend(rabbitQueue, mailParams);
    }
}
