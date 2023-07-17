package ru.khananov.services.impl.rabbitservicesimpl;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.khananov.dto.MailParams;
import ru.khananov.models.domains.TemporalCodeCache;
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
        TemporalCodeCache.getInstance().addCode(
                cryptoTool.valueOf(mailParams.getId()),
                cryptoTool.valueOf(mailParams.getTempPassword())
        );
    }
}
