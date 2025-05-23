package com.pinapp.clientservice.service;

import com.pinapp.clientservice.dto.CustomerBirthdayDTO;
import com.pinapp.clientservice.exception.CustomerNotFoundException;
import com.pinapp.clientservice.messaging.publisher.ClientEventPublisher;
import com.pinapp.clientservice.model.Customer;
import com.pinapp.clientservice.dto.CustomerDTO;
import com.pinapp.clientservice.dto.CustomerMetricsDTO;
import com.pinapp.clientservice.repository.CustomerRepository;
import com.pinapp.clientservice.util.AppLogger;
import com.pinapp.clientservice.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private AppLogger logger;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ClientEventPublisher clientEventPublisher;

    public Customer createCustomer(CustomerDTO customerDTO) {
        logger.info("Creating customer: " + customerDTO.getName());
        Customer newCustomer = buildCustomer(customerDTO);
        newCustomer = customerRepository.save(newCustomer);

        clientEventPublisher.publishClientCreatedEvent(customerDTO);

        return newCustomer;
    }

    public List<CustomerBirthdayDTO> listAllCustomers() {
        logger.info("Obtaining all customers...");
        return mapCustomersToBirthdayDTO(customerRepository.findAll());
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

    public static List<CustomerBirthdayDTO> mapCustomersToBirthdayDTO(List<Customer> customers) {
        LocalDate today = LocalDate.now();

        return customers.stream()
                .map(customer -> {
                    LocalDate birthDate = customer.getBirthDay();

                    // Set birthday for the current year
                    LocalDate nextBirthday = birthDate.withYear(today.getYear());

                    // If already passed or today, use next year
                    if (!nextBirthday.isAfter(today)) {
                        nextBirthday = nextBirthday.plusYears(1);
                    }

                    int daysUntilBirthday = (int) ChronoUnit.DAYS.between(today, nextBirthday);

                    return new CustomerBirthdayDTO(customer, daysUntilBirthday);
                })
                .collect(Collectors.toList());
    }

}
