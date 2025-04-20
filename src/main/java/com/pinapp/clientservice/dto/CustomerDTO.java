package com.pinapp.clientservice.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO implements Serializable {

    @NotBlank(message = "name cannot be null or empty")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Name can only contain letters")
    private String name;

    @NotBlank(message = "lastName cannot be null or empty")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Lastname can only contain letters")
    private String lastName;

    @NotNull(message = "age cannot be null.")
    @Min(value = 0, message = "Age cannot negative")
    @Max(value = 150, message = "Age cannot be higher than 150")
    private Integer age;

    @NotNull(message = "birthday cannot be null.")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Date must have the format: yyyy-MM-dd")
    private String birthday;
}
