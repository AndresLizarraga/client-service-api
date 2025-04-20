package com.pinapp.clientservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterRequestDTO {

    @NotBlank(message = "username cannot be null or empty")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Can only use letters and numbers")
    private String username;

    @NotBlank(message = "password cannot be null or empty")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Can only use letters and numbers")
    private String password;

    @NotBlank(message = "username cannot be null or empty")
    @Pattern(regexp = "^(admin|user)$", message = "Role can only be 'admin' o 'user'")
    private String role;
}
