package ru.khananov.services.impl.rabbitservicesimpl;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.khananov.dto.MailParamsDto;
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
    @RabbitListener(queues = "mail_answer_queue")
    public void consume(MailParamsDto mailParamsDto) {
        TemporalCodeCache.getInstance().addCode(
                cryptoTool.valueOf(mailParamsDto.getId()),
                cryptoTool.valueOf(mailParamsDto.getTempPassword())
        );
    }
}
