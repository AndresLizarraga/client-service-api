package com.pinapp.clientservice.messaging.listener;

import com.pinapp.clientservice.dto.CustomerDTO;
import com.pinapp.clientservice.util.AppLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ClientCreatedListenerTest {

    @Mock
    private AppLogger appLogger;

    @InjectMocks
    private ClientCreatedListener listener;

    @BeforeEach
    void setUp() {
        appLogger = mock(AppLogger.class);
        listener = new ClientCreatedListener(appLogger);
    }

    @Test
    void handleClientCreated_shouldLogCustomerInfo() {
        CustomerDTO dto = CustomerDTO.builder()
                .name("Alice")
                .lastName("Smith")
                .age(28)
                .birthday("1995-04-10")
                .build();

        listener.handleClientCreated(dto);

        ArgumentCaptor<String> logCaptor = ArgumentCaptor.forClass(String.class);
        verify(appLogger, times(1)).info(logCaptor.capture());

        String logMessage = logCaptor.getValue();
        assertTrue(logMessage.contains("Alice"));
        assertTrue(logMessage.contains("Smith"));
    }
}
