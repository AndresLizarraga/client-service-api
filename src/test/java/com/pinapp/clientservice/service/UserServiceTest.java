package com.pinapp.clientservice.service;

import com.pinapp.clientservice.dto.RegisterRequestDTO;
import com.pinapp.clientservice.exception.RoleNotFoundException;
import com.pinapp.clientservice.exception.UserNotValidException;
import com.pinapp.clientservice.model.Role;
import com.pinapp.clientservice.model.User;
import com.pinapp.clientservice.repository.RoleRepository;
import com.pinapp.clientservice.repository.UserRepository;
import com.pinapp.clientservice.util.AppLogger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AppLogger appLogger;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldFindUserByUsername() {
        String username = "admin";
        User user = User.builder()
                        .username(username)
                                .build();


        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        User result = userService.findUserByUsername(username);

        Assertions.assertEquals(username, result.getUsername());
        Mockito.verify(appLogger).info("Obtaining user with username: " + username);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        String username = "missingUser";
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            userService.findUserByUsername(username);
        });
    }

    @Test
    void shouldCreateUserSuccessfully() {
        // Arrange
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setUsername("newuser");
        request.setPassword("password");
        request.setRole("user");

        Role role = new Role();
        role.setId(2L);
        role.setName("USER");

        String encodedPassword = "$2a$10$encodedPassword";

        Mockito.when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        Mockito.when(roleRepository.findByName("USER")).thenReturn(Optional.of(role));
        Mockito.when(passwordEncoder.encode("password")).thenReturn(encodedPassword);

        // Act
        User created = userService.createUser(request);

        // Assert
        Assertions.assertEquals("newuser", created.getUsername());
        Assertions.assertEquals(encodedPassword, created.getPassword());
        Assertions.assertEquals(role, created.getRole());
        Mockito.verify(userRepository).save(created);
    }

    @Test
    void shouldThrowWhenUsernameAlreadyExists() {
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setUsername("existingUser");

        Mockito.when(userRepository.findByUsername("existingUser"))
                .thenReturn(Optional.of(new User()));

        Assertions.assertThrows(UserNotValidException.class, () -> {
            userService.createUser(request);
        });
    }

    @Test
    void shouldThrowWhenRoleNotFound() {
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setUsername("user");
        request.setPassword("1234");
        request.setRole("MISSING_ROLE");

        Mockito.when(userRepository.findByUsername("user")).thenReturn(Optional.empty());
        Mockito.when(roleRepository.findByName("MISSING_ROLE")).thenReturn(Optional.empty());

        Assertions.assertThrows(RoleNotFoundException.class, () -> {
            userService.createUser(request);
        });
    }
}
