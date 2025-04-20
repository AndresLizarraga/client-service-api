package com.pinapp.clientservice.repository;

import com.pinapp.clientservice.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepository  extends JpaRepository<Customer, Long> {

    @Query("SELECT AVG(c.age) FROM Customer c")
    Double obtainAgeAvg();

    @Query("SELECT SQRT(SUM(POWER(c.age - (SELECT AVG(c2.age) FROM Customer c2), 2)) / COUNT(c)) FROM Customer c")
    Double obtainAgeStandardDeviation();
}
