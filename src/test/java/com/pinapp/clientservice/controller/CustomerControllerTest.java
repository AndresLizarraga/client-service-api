package com.pinapp.clientservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pinapp.clientservice.dto.CustomerDTO;
import com.pinapp.clientservice.dto.CustomerMetricsDTO;
import com.pinapp.clientservice.exception.CustomerNotFoundException;
import com.pinapp.clientservice.model.Customer;
import com.pinapp.clientservice.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerService customerService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldReturnAllCustomers() throws Exception {
        List<Customer> mockCustomers = List.of(
                Customer.builder().id(1L).name("Ana").build(),
                Customer.builder().id(2L).name("Luis").build()
        );

        when(customerService.listAllCustomers()).thenReturn(mockCustomers);

        mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Ana"))
                .andExpect(jsonPath("$[1].name").value("Luis"));
    }

    @Test
    void shouldReturnCustomerMetrics() throws Exception {
        CustomerMetricsDTO dto = CustomerMetricsDTO.builder()
                .ageAvg(30.0)
                .ageStandardDeviation(5.0)
                .build();

        when(customerService.obtainCustomerMetrics()).thenReturn(dto);

        mockMvc.perform(get("/customers/metrics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ageAvg").value(30.0))
                .andExpect(jsonPath("$.ageStandardDeviation").value(5.0));
    }

    @Test
    void shouldCreateCustomerSuccessfully() throws Exception {
        CustomerDTO dto = CustomerDTO.builder()
                .name("Andres")
                .lastName("Lizarraga")
                .age(30)
                .birthday("1994-04-19")
                .build();

        Customer savedCustomer = Customer.builder().id(1L).name("Andres").build();

        when(customerService.createCustomer(any(CustomerDTO.class))).thenReturn(savedCustomer);

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.name").value("Andres"));
    }

    @Test
    void shouldReturnCustomerById() throws Exception {
        Customer customer = Customer.builder().id(1L).name("Mia").build();

        when(customerService.findCustomerById(1L)).thenReturn(customer);

        mockMvc.perform(get("/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Mia"));
    }

    @Test
    void shouldDeleteCustomerSuccessfully() throws Exception {
        doNothing().when(customerService).deleteCustomerById(1L);

        mockMvc.perform(delete("/customers/delete/1"))
                .andExpect(status().isAccepted());
    }

    @Test
    void shouldReturnNotFoundIfCustomerDoesNotExist() throws Exception {
        when(customerService.findCustomerById(99L)).thenThrow(new CustomerNotFoundException("Customer not found"));

        mockMvc.perform(get("/customers/99"))
                .andExpect(status().isNotFound());
    }
}
