package com.pinapp.clientservice.controller;

import com.pinapp.clientservice.model.Customer;
import com.pinapp.clientservice.dto.CustomerDTO;
import com.pinapp.clientservice.dto.CustomerMetricsDTO;
import com.pinapp.clientservice.service.CustomerService;
import com.pinapp.clientservice.util.ControllerUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/customers")
@Tag(name = "Customers", description = "Operations related to customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping
    @Operation(summary = "List Customers", description = "Returns a list with all the registered customers")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerService.listAllCustomers());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/metrics")
    @Operation(summary = "Calculate Customer Metrics", description = "Calculates the age average and the age standard deviation metrics")
    public ResponseEntity<CustomerMetricsDTO> getCustomersMetrics() {
        return ResponseEntity.ok(customerService.obtainCustomerMetrics());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Create a Customer", description = "Register a new customer in the system")
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        Customer customer = customerService.createCustomer(customerDTO);
        URI location = ControllerUtils.buildResourceLocation(customer.getId());
        return ResponseEntity
                .created(location)
                .body(customerDTO);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}")
    @Operation(summary = "Get Customer By Id", description = "Returns a customer by its id")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        Customer customer = customerService.findCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete Customer By Id", description = "Deletes a customer by its id")
    public ResponseEntity<Void> deleteCustomerbyId(@PathVariable Long id) {
        customerService.deleteCustomerById(id);
        return ResponseEntity.accepted().build();
    }
}
