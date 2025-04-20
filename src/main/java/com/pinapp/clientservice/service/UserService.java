package com.pinapp.clientservice.service;

import com.pinapp.clientservice.dto.RegisterRequestDTO;
import com.pinapp.clientservice.exception.RoleNotFoundException;
import com.pinapp.clientservice.exception.UserNotValidException;
import com.pinapp.clientservice.model.Role;
import com.pinapp.clientservice.model.User;
import com.pinapp.clientservice.repository.RoleRepository;
import com.pinapp.clientservice.repository.UserRepository;
import com.pinapp.clientservice.util.AppLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private AppLogger logger;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User findUserByUsername(String username) {
        logger.info("Obtaining user with username: " + username);
        return userRepository.findByUsername(username)
                .orElseThrow( ()-> new UsernameNotFoundException("Username: " + username + " not found"));
    }

    public User createUser(RegisterRequestDTO registerRequestDTO) {
        logger.info("Creating user: " + registerRequestDTO.getUsername());

        if (userRepository.findByUsername(registerRequestDTO.getUsername()).isPresent()) {
            throw new UserNotValidException("Username already exists");
        }

        Role role = roleRepository.findByName(registerRequestDTO.getRole().toUpperCase())
                .orElseThrow(() -> new RoleNotFoundException("Role not found"));

        User user = User.builder()
                .username(registerRequestDTO.getUsername())
                .password(passwordEncoder.encode(registerRequestDTO.getPassword()))
                .role(role)
                .build();

        userRepository.save(user);

        return user;
    }

}
