package com.pinapp.clientservice.advice;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.pinapp.clientservice.testdummy.DummyExceptionController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = DummyExceptionController.class)
@Import(GlobalExceptionHandler.class)
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
}
