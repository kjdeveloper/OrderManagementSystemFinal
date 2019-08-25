package com.app.service.services;

import com.app.dto.CustomerDto;
import com.app.exceptions.MyException;
import com.app.model.Country;
import com.app.model.Customer;
import com.app.repository.CountryRepository;
import com.app.repository.CustomerRepository;
import com.app.repository.impl.CountryRepositoryImpl;
import com.app.repository.impl.CustomerRepositoryImpl;
import com.app.service.mapper.Mappers;
import com.app.validation.impl.CountryValidator;
import com.app.validation.impl.CustomerValidator;

import java.util.*;
import java.util.stream.Collectors;

public class CustomerService {

    private final CustomerRepository customerRepository = new CustomerRepositoryImpl();
    private final CustomerValidator customerDtoValidator = new CustomerValidator();
    private final CountryValidator countryValidator = new CountryValidator();
    private final CountryRepository countryRepository = new CountryRepositoryImpl();

    public CustomerDto addCustomer(final CustomerDto customerDto) {
        customerDtoValidator.validateCustomer(customerDto);

        final boolean exist = customerRepository.isExistByNameAndSurnameAndCountry(customerDto);

        if (exist) {
            throw new MyException("CUSTOMER WITH GIVEN NAME, SURNAME AND COUNTRY IS ALREADY EXIST");
        }

        Country country = countryRepository.findByName(customerDto.getCountryDto().getName()).orElse(null);

        if (country == null) {
            countryValidator.validateCountry(customerDto.getCountryDto());
            country = Mappers.fromCountryDtoToCountry(customerDto.getCountryDto());
            country = countryRepository.addOrUpdate(country).orElseThrow(() -> new MyException("CAN NOT ADD COUNTRY IN CUSTOMER SERVICE"));
        }

        Customer customer = Mappers.fromCustomerDtoToCustomer(customerDto);
        customer.setCountry(country);
        customerRepository.addOrUpdate(customer);

        return Mappers.fromCustomerToCustomerDto(customer);
    }

    public Map<Country, List<String>> findCustomersWhoOrderedProductWithSameCountryAsTheir() {

        return customerRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(Customer::getCountry, Collectors.toList()))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        k -> k.getValue()
                                .stream()
                                .map(Customer::getName)
                                .filter(c -> k.getKey().getName().equals(c)).collect(Collectors.toList()),
                        (k, v) -> k,
                        LinkedHashMap::new
                ));

    }
}
