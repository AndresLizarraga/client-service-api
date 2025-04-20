package com.pinapp.clientservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FieldErrorDTO {

    private String field;
    private String message;
}
