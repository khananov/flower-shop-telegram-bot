package ru.khananov.services;

import ru.khananov.models.entities.UserEntity;

public interface UserEntityService {
    UserEntity findByUsername(String username);
}
