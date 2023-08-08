package ru.khananov.services.impl.rabbitimpl;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.khananov.dto.MailParamsDto;
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
    @RabbitListener(queues = "mail_verification_queue")
    public void consumerMailParams(MailParamsDto mailParamsDto) {
        mailSenderService.send(mailParamsDto);
    }
}