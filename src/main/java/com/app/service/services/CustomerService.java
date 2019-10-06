package com.app.service.services;

import com.app.dto.CustomerDto;
import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.app.repository.CountryRepository;
import com.app.repository.CustomerRepository;
import com.app.service.mapper.Mappers;
import com.app.validation.impl.CountryValidator;
import com.app.validation.impl.CustomerValidator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CountryRepository countryRepository;

    public CustomerDto addCustomer(final CustomerDto customerDto) {
        CustomerValidator customerDtoValidator = new CustomerValidator();
        CountryValidator countryValidator = new CountryValidator();
        customerDtoValidator.validateCustomer(customerDto);

        final var exist = customerRepository.isExistByNameAndSurnameAndCountry(customerDto);

        if (exist) {
            throw new MyException(ExceptionCode.CUSTOMER, "CUSTOMER WITH GIVEN NAME, SURNAME AND COUNTRY IS ALREADY EXIST");
        }

        var country = countryRepository.findByName(customerDto.getCountryDto().getName()).orElse(null);

        if (country == null) {
            countryValidator.validateCountry(customerDto.getCountryDto());
            country = Mappers.fromCountryDtoToCountry(customerDto.getCountryDto());
            country = countryRepository.addOrUpdate(country).orElseThrow(() -> new MyException(ExceptionCode.COUNTRY, "CANNOT ADD COUNTRY IN CUSTOMER SERVICE"));
        }

        var customer = Mappers.fromCustomerDtoToCustomer(customerDto);
        customer.setCountry(country);
        customerRepository.addOrUpdate(customer);

        return Mappers.fromCustomerToCustomerDto(customer);
    }
}
