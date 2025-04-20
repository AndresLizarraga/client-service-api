package com.pinapp.clientservice.testdummy;

import com.pinapp.clientservice.exception.CustomerNotFoundException;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
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

    @Getter
    @Setter
    public static class DummyDTO {
        @NotBlank(message = "name is required")
        private String name;
    }
}
