package ru.khananov.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.khananov.repositories.TelegramUserRepository;
import ru.khananov.services.TelegramUserService;

@Service
public class TelegramUserServiceImpl implements TelegramUserService {
    private final TelegramUserRepository userRepository;

    @Autowired
    public TelegramUserServiceImpl(TelegramUserRepository userRepository) {
        this.userRepository = userRepository;
    }
}