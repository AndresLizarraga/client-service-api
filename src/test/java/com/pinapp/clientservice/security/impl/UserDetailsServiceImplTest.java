package com.pinapp.clientservice.security.impl;

import com.pinapp.clientservice.model.Role;
import com.pinapp.clientservice.model.User;
import com.pinapp.clientservice.repository.UserRepository;
import com.pinapp.clientservice.security.impl.UserDetailsServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void shouldLoadUserByUsername() {
        // Arrange
        String username = "admin";
        String password = "hashed-password";

        Role role = new Role();
        role.setId(1L);
        role.setName("ADMIN");

        User user = User
                .builder()
                .username(username)
                .password(password)
                .role(role)
                .build();

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Assert
        Assertions.assertEquals(username, userDetails.getUsername());
        Assertions.assertEquals(password, userDetails.getPassword());
        Assertions.assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        // Arrange
        String username = "missingUser";
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Assert
        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(username);
        });
    }
}
