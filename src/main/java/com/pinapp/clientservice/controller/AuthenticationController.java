package com.pinapp.clientservice.controller;

import com.pinapp.clientservice.dto.AuthRequestDTO;
import com.pinapp.clientservice.dto.AuthResponseDTO;
import com.pinapp.clientservice.dto.RegisterRequestDTO;
import com.pinapp.clientservice.model.User;
import com.pinapp.clientservice.security.JwtUtil;
import com.pinapp.clientservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Login and Creation of Users")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @Operation(summary = "User Login", description = "Allows to authenticate an user persisted on the db")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userService.findUserByUsername(request.getUsername());

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().getName());
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }

    @PostMapping("/register")
    @Operation(summary = "User Registration", description = "Allows to register a new user on the db")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequestDTO request) {

        User newUser = userService.createUser(request);

        return ResponseEntity.ok("User registered successfully");
    }
}
