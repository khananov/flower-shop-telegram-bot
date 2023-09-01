package ru.khananov.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import ru.khananov.dto.MailParamsDto;
import ru.khananov.exceptions.EmailIsNullException;
import ru.khananov.services.rabbit.MailProducerService;
import ru.khananov.utils.CryptoTool;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MailSenderServiceImplTest {

    @InjectMocks
    private MailSenderServiceImpl mailSenderService;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private MailProducerService mailProducerService;

    @Mock
    private CryptoTool cryptoTool;

    private MailParamsDto mailParamsDto;

    @BeforeEach
    public void setUp() {
        mailParamsDto = new MailParamsDto();
    }

    @Test
    void send_emailIsNull_throwsException() {
        assertThrows(EmailIsNullException.class,
                () -> mailSenderService.send(mailParamsDto));
    }

    @Test
    void send_correctEmail_successfulProduceMessage() {
        mailParamsDto.setId("1");
        mailParamsDto.setEmailTo("email");

        mailSenderService.send(mailParamsDto);
        Mockito.verify(mailProducerService, Mockito.times(1))
                .produceMailParam("mail_answer_queue", mailParamsDto);
    }
}