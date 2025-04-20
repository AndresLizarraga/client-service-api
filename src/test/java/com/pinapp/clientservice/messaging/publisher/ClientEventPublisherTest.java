package com.pinapp.clientservice.messaging.publisher;

import com.pinapp.clientservice.config.RabbitMQConfig;
import com.pinapp.clientservice.dto.CustomerDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.Mockito.*;

public class ClientEventPublisherTest {

    private RabbitTemplate rabbitTemplate;
    private ClientEventPublisher clientEventPublisher;

    @BeforeEach
    void setUp() {
        rabbitTemplate = mock(RabbitTemplate.class);
        clientEventPublisher = new ClientEventPublisher(rabbitTemplate);
    }

    @Test
    void publishClientCreatedEvent_shouldSendMessageToRabbitMQ() {
        CustomerDTO customerDTO = CustomerDTO.builder()
                .name("John")
                .lastName("Doe")
                .age(30)
                .birthday("1990-01-01")
                .build();

        clientEventPublisher.publishClientCreatedEvent(customerDTO);

        verify(rabbitTemplate, times(1)).convertAndSend(
                eq(RabbitMQConfig.EXCHANGE),
                eq(RabbitMQConfig.ROUTING_KEY),
                eq(customerDTO)
        );
    }
}
