package com.app.repository;

import com.app.dto.CustomerDto;
import com.app.model.Customer;
import com.app.repository.generic.GenericRepository;

import java.util.Optional;

public interface CustomerRepository extends GenericRepository<Customer> {
    Optional<Customer> findByName(String name);

    Optional<Customer> findBySurname(String surname);

    boolean isExistByNameAndSurnameAndCountry(CustomerDto customerDTO);
}
