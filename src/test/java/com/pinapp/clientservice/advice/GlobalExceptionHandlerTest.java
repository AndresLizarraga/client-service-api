package com.pinapp.clientservice.advice;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.pinapp.clientservice.testdummy.DummyExceptionController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = DummyExceptionController.class)
@Import(GlobalExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnNotFoundForCustomException() throws Exception {
        mockMvc.perform(get("/exception-test/notfound"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT FOUND"))
                .andExpect(jsonPath("$.message").value("Customer not found!"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void shouldReturnBadRequestForValidationException() throws Exception {
        String invalidBody = "{}";

        mockMvc.perform(post("/exception-test/badrequest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("BAD REQUEST"))
                .andExpect(jsonPath("$.message").value("FIELD VALIDATION ERROR"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.fieldErrorsList").isArray())
                .andExpect(jsonPath("$.fieldErrorsList[0].field").value("name"))
                .andExpect(jsonPath("$.fieldErrorsList[0].message").value("name is required"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void shouldReturnUnauthorizedForBadCredentials() throws Exception {
        mockMvc.perform(get("/exception-test/unauthorized"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.message").value("Invalid credentials"));
    }

    @Test
    void shouldReturnForbiddenForAccessDenied() throws Exception {
        mockMvc.perform(get("/exception-test/access-denied"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("FORBIDDEN"))
                .andExpect(jsonPath("$.message").value("Access denied"));
    }

    @Test
    void shouldReturnUnauthorizedForUsernameNotFound() throws Exception {
        mockMvc.perform(get("/exception-test/username-not-found"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.message").value("Username not found"));
    }

    @Test
    void shouldReturnUnauthorizedForJwtException() throws Exception {
        mockMvc.perform(get("/exception-test/jwt-error"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.message").value("Invalid or expired token"));
    }

    @Test
    void shouldReturnBadRequestForUserNotValidException() throws Exception {
        mockMvc.perform(get("/exception-test/user-invalid"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("BAD REQUEST"))
                .andExpect(jsonPath("$.message").value("Username already exists"));
    }

    @Test
    void shouldReturnNotFoundForRoleNotFoundException() throws Exception {
        mockMvc.perform(get("/exception-test/role-not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT FOUND"))
                .andExpect(jsonPath("$.message").value("Role not found"));
    }
}
