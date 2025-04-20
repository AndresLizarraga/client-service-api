package com.pinapp.clientservice.service;

import com.pinapp.clientservice.exception.CustomerNotFoundException;
import com.pinapp.clientservice.model.Customer;
import com.pinapp.clientservice.dto.CustomerDTO;
import com.pinapp.clientservice.dto.CustomerMetricsDTO;
import com.pinapp.clientservice.repository.CustomerRepository;
import com.pinapp.clientservice.util.AppLogger;
import com.pinapp.clientservice.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private AppLogger logger;

    @Autowired
    private CustomerRepository customerRepository;

    public Customer createCustomer(CustomerDTO customerDTO) {
        logger.info("Creating customer: " + customerDTO.getName());
        Customer newCustomer = buildCustomer(customerDTO);
        return customerRepository.save(newCustomer);
    }

    public List<Customer> listAllCustomers() {
        logger.info("Obtaining all customers...");
        return customerRepository.findAll();
    }

    public Customer findCustomerById(Long id) {
        logger.info("Obtaining customer with id: " + id);
        return customerRepository.findById(id)
                .orElseThrow( ()-> new CustomerNotFoundException("Customer with id: " + id + " not found"));
    }

    public void deleteCustomerById(Long id) {
        logger.info("Deleting customer with id: " + id);
        Customer newCustomer = customerRepository.findById(id)
                .orElseThrow( ()-> new CustomerNotFoundException("customer with id: " + id + " not found"));
        customerRepository.delete(newCustomer);
    }

    private Customer buildCustomer(CustomerDTO customerDTO) {
        return Customer.builder()
                .age(customerDTO.getAge())
                .name(customerDTO.getName())
                .lastName(customerDTO.getLastName())
                .birthDay(DateUtils.parseDate(customerDTO.getBirthday()))
                .build();
    }

    public CustomerMetricsDTO obtainCustomerMetrics() {
        logger.info("Calculating customer metrics...");
        final Double ageAvg = customerRepository.obtainAgeAvg();
        final Double ageStandardDeviation = customerRepository.obtainAgeStandardDeviation();
        return CustomerMetricsDTO.builder()
                .ageAvg(ageAvg != null ? ageAvg : 0.00)
                .ageStandardDeviation(ageStandardDeviation != null ? ageStandardDeviation : 0.00)
                .build();
    }

}
