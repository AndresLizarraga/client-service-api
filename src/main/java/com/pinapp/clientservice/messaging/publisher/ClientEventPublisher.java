package com.pinapp.clientservice.messaging.publisher;

import com.pinapp.clientservice.config.RabbitMQConfig;
import com.pinapp.clientservice.dto.CustomerDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ClientEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public ClientEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishClientCreatedEvent(CustomerDTO customerDTO) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                customerDTO
        );
    }
}
