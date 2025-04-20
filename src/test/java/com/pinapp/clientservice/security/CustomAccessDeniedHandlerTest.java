package com.pinapp.clientservice.security;

import com.pinapp.clientservice.security.handler.CustomAccessDeniedHandler;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;

public class CustomAccessDeniedHandlerTest {

    private final CustomAccessDeniedHandler handler = new CustomAccessDeniedHandler();

    @Test
    void shouldReturn403WithCustomMessage() throws Exception {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        AccessDeniedException exception = new AccessDeniedException("Access denied");

        // Act
        handler.handle(request, response, exception);

        // Assert
        Assertions.assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
        Assertions.assertEquals("application/json", response.getContentType());
        Assertions.assertEquals(
                "{ \"message\": \"You do not have permission to access this resource.\" }",
                response.getContentAsString()
        );
    }
}
