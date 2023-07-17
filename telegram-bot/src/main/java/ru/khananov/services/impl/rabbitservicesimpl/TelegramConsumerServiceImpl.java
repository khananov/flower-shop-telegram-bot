package ru.khananov.services.impl.rabbitservicesimpl;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.khananov.dto.MailParams;
import ru.khananov.services.rabbitservices.TelegramConsumerService;
import ru.khananov.utils.CryptoTool;

@Service
public class TelegramConsumerServiceImpl implements TelegramConsumerService {
    private final CryptoTool cryptoTool;

    @Autowired
    public TelegramConsumerServiceImpl(CryptoTool cryptoTool) {
        this.cryptoTool = cryptoTool;
    }

    @Override
    @RabbitListener(queues = "MAIL_ANSWER_QUEUE")
    public void consume(MailParams mailParams) {
        System.out.println(mailParams.getId() + " id - encode ");
        System.out.println(cryptoTool.valueOf(mailParams.getId()) + " id - decode");
        System.out.println(mailParams.getTempPassword() + " password - encode");
        System.out.println(cryptoTool.valueOf(mailParams.getTempPassword()) + " password - decode");
    }
}
