package ru.khananov.services.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.khananov.models.entities.UserEntity;
import ru.khananov.exceptions.UserNotFoundException;
import ru.khananov.repositories.UserEntityRepository;
import ru.khananov.services.UserEntityService;

@Service
@Log4j2
public class UserEntityServiceImpl implements UserEntityService {
    private final UserEntityRepository userEntityRepository;

    @Autowired
    public UserEntityServiceImpl(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }

    @Override
    public UserEntity findByUsername(String username) {
        return userEntityRepository.findByUsername(username)
                .orElseGet(() -> {
                    log.error(new UserNotFoundException(username));
                    throw new UserNotFoundException(username);
                });
    }
}
