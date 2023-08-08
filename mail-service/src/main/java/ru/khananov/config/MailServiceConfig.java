package ru.khananov.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.khananov.utils.CryptoTool;

@Configuration
public class MailServiceConfig {
    @Value("${salt}")
    private String salt;

    @Bean
    public CryptoTool getCryptoTool() {
        return new CryptoTool(salt);
    }
}