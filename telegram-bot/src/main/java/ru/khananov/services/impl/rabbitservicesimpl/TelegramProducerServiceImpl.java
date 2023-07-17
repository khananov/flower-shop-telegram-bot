package ru.khananov.services.impl.rabbitservicesimpl;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.khananov.dto.MailParams;
import ru.khananov.services.rabbitservices.TelegramProducerService;
import ru.khananov.utils.CryptoTool;

@Service
public class TelegramProducerServiceImpl implements TelegramProducerService {
    private final CryptoTool cryptoTool;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public TelegramProducerServiceImpl(CryptoTool cryptoTool, RabbitTemplate rabbitTemplate) {
        this.cryptoTool = cryptoTool;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void produce(String rabbitQueue, Long chatId, String email) {
        MailParams mailParams = MailParams.builder()
                .id(cryptoTool.hashOf(chatId))
                .emailTo(email)
                .build();

        rabbitTemplate.convertAndSend(rabbitQueue, mailParams);
    }
}
