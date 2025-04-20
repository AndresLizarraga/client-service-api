package com.pinapp.clientservice.repository;

import com.pinapp.clientservice.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class CustomerRepositoryIntegrationTest {

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        Customer c1 = Customer.builder().name("Ana").lastName("Gomez").age(30).birthDay(LocalDate.of(1994, 4, 19)).build();
        Customer c2 = Customer.builder().name("Luis").lastName("Pérez").age(40).birthDay(LocalDate.of(1984, 8, 10)).build();
        Customer c3 = Customer.builder().name("Mia").lastName("Ramirez").age(20).birthDay(LocalDate.of(2004, 2, 5)).build();

        customerRepository.saveAll(List.of(c1, c2, c3));
    }

    @Test
    void shouldCalculateAgeAverageCorrectly() {
        Double avg = customerRepository.obtainAgeAvg();
        assertEquals(30.0, avg);
    }

    @Test
    void shouldCalculateAgeStandardDeviationCorrectly() {
        Double std = customerRepository.obtainAgeStandardDeviation();

        // The standard deviation of [30, 40, 20] is ≈ 8.16
        assertTrue(std > 8.0 && std < 8.2);
    }
}
