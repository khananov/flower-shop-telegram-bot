package ru.khananov.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.khananov.models.entities.TelegramUser;

@Repository
public interface TelegramUserRepository extends JpaRepository<TelegramUser, Long> {
}
