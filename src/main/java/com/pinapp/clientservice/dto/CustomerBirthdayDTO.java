package com.pinapp.clientservice.dto;

import com.pinapp.clientservice.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerBirthdayDTO {

    private Customer customer;
    private Integer daysUntilBirthday;
}
