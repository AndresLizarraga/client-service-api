package com.pinapp.clientservice.messaging.listener;

import com.pinapp.clientservice.config.RabbitMQConfig;
import com.pinapp.clientservice.dto.CustomerDTO;
import com.pinapp.clientservice.util.AppLogger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClientCreatedListener {

    private final AppLogger appLogger;

    @Autowired
    public ClientCreatedListener(AppLogger appLogger) {
        this.appLogger = appLogger;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void handleClientCreated(CustomerDTO customerDTO) {
        appLogger.info("Audited Customer from RabbitMQ: " + customerDTO.getName() + " "  + customerDTO.getLastName());
    }
}
