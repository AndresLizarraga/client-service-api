package com.pinapp.clientservice.service;

import com.pinapp.clientservice.dto.CustomerBirthdayDTO;
import com.pinapp.clientservice.dto.CustomerDTO;
import com.pinapp.clientservice.dto.CustomerMetricsDTO;
import com.pinapp.clientservice.exception.CustomerNotFoundException;
import com.pinapp.clientservice.messaging.publisher.ClientEventPublisher;
import com.pinapp.clientservice.model.Customer;
import com.pinapp.clientservice.repository.CustomerRepository;
import com.pinapp.clientservice.util.AppLogger;
import com.pinapp.clientservice.util.DateUtils;
import com.pinapp.clientservice.util.DateUtilsTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AppLogger logger;

    @Mock
    private ClientEventPublisher clientEventPublisher;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void shouldCreateCustomerSuccessfully() {
        CustomerDTO dto = CustomerDTO.builder()
                .name("Andres")
                .lastName("Lizarraga")
                .age(30)
                .birthday("1994-04-19")
                .build();

        Customer savedCustomer = Customer.builder()
                .id(1L)
                .name("Andres")
                .lastName("Lizarraga")
                .age(30)
                .birthDay(DateUtils.parseDate("1994-04-19"))
                .build();

        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        doNothing().when(clientEventPublisher).publishClientCreatedEvent(any(CustomerDTO.class));

        Customer result = customerService.createCustomer(dto);

        assertNotNull(result);
        assertEquals("Andres", result.getName());
        assertEquals(30, result.getAge());
        verify(logger).info("Creating customer: Andres");
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void shouldListAllCustomers() {
        Customer savedCustomer = Customer.builder()
                .id(1L)
                .name("Andres")
                .lastName("Lizarraga")
                .age(30)
                .birthDay(DateUtils.parseDate("1994-04-19"))
                .build();

        Customer savedCustomer2 = Customer.builder()
                .id(1L)
                .name("Carlos")
                .lastName("Perez")
                .age(35)
                .birthDay(DateUtils.parseDate("1990-01-19"))
                .build();

        List<Customer> mockCustomers = List.of(savedCustomer, savedCustomer2);
        when(customerRepository.findAll()).thenReturn(mockCustomers);

        List<CustomerBirthdayDTO> result = customerService.listAllCustomers();

        assertEquals(2, result.size());
        verify(logger).info("Obtaining all customers...");
    }

    @Test
    void shouldThrowExceptionWhenCustomerNotFound() {
        Long id = 100L;
        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.findCustomerById(id));
        verify(logger).info("Obtaining customer with id: " + id);
    }

    @Test
    void shouldReturnCustomerWhenFound() {
        Customer customer = new Customer();
        customer.setId(1L);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Customer result = customerService.findCustomerById(1L);

        assertEquals(1L, result.getId());
        verify(logger).info("Obtaining customer with id: 1");
    }

    @Test
    void shouldReturnCustomerMetricsWithDefaults() {
        when(customerRepository.obtainAgeAvg()).thenReturn(null);
        when(customerRepository.obtainAgeStandardDeviation()).thenReturn(null);

        CustomerMetricsDTO result = customerService.obtainCustomerMetrics();

        assertEquals(0.00, result.getAgeAvg());
        assertEquals(0.00, result.getAgeStandardDeviation());
        verify(logger).info("Calculating customer metrics...");
    }

    @Test
    void shouldDeleteCustomerByIdSuccessfully() {
        Long customerId = 1L;

        Customer existingCustomer = Customer.builder()
                .id(customerId)
                .name("Andres")
                .build();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));

        customerService.deleteCustomerById(customerId);

        verify(logger).info("Deleting customer with id: " + customerId);
        verify(customerRepository).delete(existingCustomer);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingCustomer() {
        Long customerId = 99L;

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.deleteCustomerById(customerId));

        verify(logger).info("Deleting customer with id: " + customerId);
        verify(customerRepository, never()).delete(any());
    }
}
