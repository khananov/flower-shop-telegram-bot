package ru.khananov.services;

import ru.khananov.dto.MailParamsDto;

public interface MailSenderService {
    void send(MailParamsDto mailParamsDto);
}