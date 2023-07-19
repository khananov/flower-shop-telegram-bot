package ru.khananov.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue mailVerificationQueue() {
        return new Queue("MAIL_VERIFICATION_QUEUE");
    }

    @Bean
    public Queue mailAnswerQueue() {
        return new Queue("MAIL_ANSWER_QUEUE");
    }
}