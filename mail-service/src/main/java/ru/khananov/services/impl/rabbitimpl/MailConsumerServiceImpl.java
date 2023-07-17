package ru.khananov.services.impl.rabbitimpl;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.khananov.dto.MailParams;
import ru.khananov.services.MailSenderService;
import ru.khananov.services.rabbit.MailConsumerService;

@Service
public class MailConsumerServiceImpl implements MailConsumerService {
    private final MailSenderService mailSenderService;

    @Autowired
    public MailConsumerServiceImpl(MailSenderService mailSenderService) {
        this.mailSenderService = mailSenderService;
    }

    @Override
    @RabbitListener(queues = "MAIL_VERIFICATION_QUEUE")
    public void consumerMailParams(MailParams mailParams) {
        mailSenderService.send(mailParams);
    }
}
