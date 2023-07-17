package ru.khananov.services;

import ru.khananov.dto.MailParams;

public interface MailSenderService {
    void send(MailParams mailParams);
}
