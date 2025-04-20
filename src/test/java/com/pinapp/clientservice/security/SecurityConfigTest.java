package com.pinapp.clientservice.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SecurityFilterChain securityFilterChain;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldLoadSecurityBeans() {
        Assertions.assertNotNull(securityFilterChain);
        Assertions.assertNotNull(authenticationManager);
        Assertions.assertNotNull(passwordEncoder);
    }

    @Test
    void shouldAllowAccessToLoginAndRegisterWithoutAuth() throws Exception {
        mockMvc.perform(post("/auth/login"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/auth/register"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectAccessToProtectedEndpointWithoutAuth() throws Exception {
        mockMvc.perform(get("/customers"))
                .andExpect(status().isForbidden());
    }

}
