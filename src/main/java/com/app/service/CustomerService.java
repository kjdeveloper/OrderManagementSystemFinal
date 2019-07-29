package com.app.service;

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

public class CustomerService {

    private final CustomerRepository customerRepository = new CustomerRepositoryImpl();
    private final CustomerValidator customerDtoValidator = new CustomerValidator();
    private final CountryValidator countryValidator = new CountryValidator();
    private final CountryRepository countryRepository = new CountryRepositoryImpl();

 /*   private boolean isValidCustomer(CustomerDto customerDTO) {

        Map<String, String> customerErrorsMap = customerDtoValidator.validate(customerDTO);
        if (customerDtoValidator.hasErrors()) {
            System.out.println("------CUSTOMER VALIDATION ERRORS------");
            customerErrorsMap.forEach((k, v) -> System.out.println(k + " -> " + v));
            System.out.println("--------------------------------------");
        }
        return !customerDtoValidator.hasErrors();
    }*/

    public CustomerDto addCustomer(final CustomerDto customerDto) {
        customerDtoValidator.validateCustomer(customerDto);
        countryValidator.validateCountry(customerDto.getCountryDTO());

        final boolean exist = customerRepository.isExistByNameAndSurnameAndCountry(customerDto);

        if (exist) {
            throw new MyException("CUSTOMER WITH GIVEN NAME, SURNAME AND COUNTRY IS ALREADY EXIST");
        }

        String countryName = customerDto.getCountryDTO().getName();

        Country country = null;

        if (countryName != null) {
            country = countryRepository.findByName(countryName).orElse(null);
            if (country == null) {
                country = Mappers.fromCountryDTOToCountry(customerDto.getCountryDTO());
                country = countryRepository.addOrUpdate(country).orElseThrow(() -> new MyException("CANNOT ADD COUNTRY"));
            }
        }

        Customer customer = Mappers.fromCustomerDTOToCustomer(customerDto);
        customer.setCountry(country);
        customerRepository.addOrUpdate(customer);

        return Mappers.fromCustomerToCustomerDTO(customer);
    }

}
