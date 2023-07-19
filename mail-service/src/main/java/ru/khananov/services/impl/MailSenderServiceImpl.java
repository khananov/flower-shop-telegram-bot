package ru.khananov.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.khananov.dto.MailParamsDto;
import ru.khananov.services.MailSenderService;
import ru.khananov.services.rabbit.MailProducerService;
import ru.khananov.utils.CryptoTool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MailSenderServiceImpl implements MailSenderService {
    @Value("${spring.mail.username}")
    private String mailFrom;

    private final JavaMailSender javaMailSender;
    private final MailProducerService mailProducerService;
    private final CryptoTool cryptoTool;

    @Autowired
    public MailSenderServiceImpl(JavaMailSender javaMailSender,
                                 MailProducerService mailProducerService,
                                 CryptoTool cryptoTool) {
        this.javaMailSender = javaMailSender;
        this.mailProducerService = mailProducerService;
        this.cryptoTool = cryptoTool;
    }

    @Override
    public void send(MailParamsDto mailParamsDto) {
        String subject = "Подтверждение электронной почты";
        String messageBody = getActivationMailPassword();
        String mailTo = mailParamsDto.getEmailTo();

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(mailFrom);
        mailMessage.setTo(mailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(messageBody);

        mailParamsDto.setTempPassword(cryptoTool.hashOf(Long.valueOf(messageBody)));
        mailProducerService.produceMailParam("MAIL_ANSWER_QUEUE", mailParamsDto);
        javaMailSender.send(mailMessage);
    }

    private String getActivationMailPassword() {
        List<Integer> numbers = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            numbers.add(i);
        }

        Collections.shuffle(numbers);

        StringBuilder result = new StringBuilder();
        for(int i = 0; i < 4; i++){
            result.append(numbers.get(i));
        }
        return result.toString();
    }
}
