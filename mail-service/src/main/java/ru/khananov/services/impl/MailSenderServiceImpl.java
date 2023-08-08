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
        String mailTo = mailParamsDto.getEmailTo();
        String subject = "Подтверждение электронной почты";
        String code = getActivationMailPassword();
        String messageBody = code + " - Ваш код подтверждения почты";

        mailParamsDto.setTempPassword(cryptoTool.hashOf(Long.valueOf(code)));
        mailProducerService.produceMailParam("mail_answer_queue", mailParamsDto);

        javaMailSender.send(createMailMessage(mailTo, subject, messageBody));
    }

    private String getActivationMailPassword() {
        StringBuilder result = new StringBuilder();

        for(int i = 0; i < 4; i++){
            result.append((int) (Math.random() * 10));
        }

        return result.toString();
    }

    private SimpleMailMessage createMailMessage(String mailTo, String subject, String messageBody) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(mailFrom);
        mailMessage.setTo(mailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(messageBody);

        return mailMessage;
    }
}
