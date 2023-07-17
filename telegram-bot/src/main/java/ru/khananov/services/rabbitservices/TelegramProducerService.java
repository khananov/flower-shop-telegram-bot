package ru.khananov.services.rabbitservices;

public interface TelegramProducerService {
    void produce(String rabbitQueue, Long chatId, String email);
}
