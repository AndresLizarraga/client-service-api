package com.pinapp.clientservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pinapp.clientservice.dto.AuthRequestDTO;
import com.pinapp.clientservice.dto.RegisterRequestDTO;
import com.pinapp.clientservice.model.Role;
import com.pinapp.clientservice.model.User;
import com.pinapp.clientservice.security.JwtUtil;
import com.pinapp.clientservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserService userService;

    @Test
    void shouldLoginAndReturnToken() throws Exception {
        // Arrange
        String username = "admin";
        String password = "password";
        String jwtToken = "mocked-jwt-token";

        AuthRequestDTO request = new AuthRequestDTO();
        request.setUsername(username);
        request.setPassword(password);

        Role role = new Role();
        role.setName("ADMIN");
        role.setId(1L);

        User user = User.builder()
                        .username(username)
                                .role(role)
                .build();

        Mockito.when(userService.findUserByUsername(username)).thenReturn(user);
        Mockito.when(jwtUtil.generateToken(username, "ADMIN")).thenReturn(jwtToken);

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(jwtToken));
    }

    @Test
    void shouldRegisterNewUserSuccessfully() throws Exception {
        // Arrange
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO();
        registerRequestDTO.setUsername("newuser");
        registerRequestDTO.setPassword("password");
        registerRequestDTO.setRole("admin");

        Mockito.when(userService.createUser(registerRequestDTO)).thenReturn(new User());

        // Act & Assert
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registerRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));
    }
}
