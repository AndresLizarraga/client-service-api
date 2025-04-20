package com.pinapp.clientservice.testdummy;

import com.pinapp.clientservice.exception.CustomerNotFoundException;
import com.pinapp.clientservice.exception.RoleNotFoundException;
import com.pinapp.clientservice.exception.UserNotValidException;
import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@Hidden
@RestController
@RequestMapping("/exception-test")
public class DummyExceptionController {

    @GetMapping("/notfound")
    public void throwNotFound() {
        throw new CustomerNotFoundException("Customer not found!");
    }

    @PostMapping("/badrequest")
    public void throwValidation(@Valid @RequestBody DummyDTO dto) {
    }

    @GetMapping("/unauthorized")
    public void throwBadCredentials() {
        throw new BadCredentialsException("Invalid credentials");
    }

    @GetMapping("/access-denied")
    public void throwAccessDenied() {
        throw new AccessDeniedException("Access denied");
    }

    @GetMapping("/username-not-found")
    public void throwUsernameNotFound() {
        throw new UsernameNotFoundException("Username not found");
    }

    @GetMapping("/jwt-error")
    public void throwJwtException() {
        throw new JwtException("Invalid or expired token");
    }

    @GetMapping("/user-invalid")
    public void throwUserNotValid() {
        throw new UserNotValidException("Username already exists");
    }

    @GetMapping("/role-not-found")
    public void throwRoleNotFound() {
        throw new RoleNotFoundException("Role not found");
    }

    @Getter
    @Setter
    public static class DummyDTO {
        @NotBlank(message = "name is required")
        private String name;
    }
}
