package com.app.service.services;

import com.app.dto.CustomerDto;
import com.app.dto.CustomerOrderDto;
import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.app.model.Country;
import com.app.model.Customer;
import com.app.repository.CountryRepository;
import com.app.repository.CustomerOrderRepository;
import com.app.repository.CustomerRepository;
import com.app.repository.impl.CountryRepositoryImpl;
import com.app.repository.impl.CustomerOrderRepositoryImpl;
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
    private final CustomerOrderRepository customerOrderRepository = new CustomerOrderRepositoryImpl();

    public CustomerDto addCustomer(final CustomerDto customerDto) {
        customerDtoValidator.validateCustomer(customerDto);

        final boolean exist = customerRepository.isExistByNameAndSurnameAndCountry(customerDto);

        if (exist) {
            throw new MyException(ExceptionCode.CUSTOMER, "CUSTOMER WITH GIVEN NAME, SURNAME AND COUNTRY IS ALREADY EXIST");
        }

        Country country = countryRepository.findByName(customerDto.getCountryDto().getName()).orElse(null);

        if (country == null) {
            countryValidator.validateCountry(customerDto.getCountryDto());
            country = Mappers.fromCountryDtoToCountry(customerDto.getCountryDto());
            country = countryRepository.addOrUpdate(country).orElseThrow(() -> new MyException(ExceptionCode.COUNTRY, "CAN NOT ADD COUNTRY IN CUSTOMER SERVICE"));
        }

        Customer customer = Mappers.fromCustomerDtoToCustomer(customerDto);
        customer.setCountry(country);
        customerRepository.addOrUpdate(customer);

        return Mappers.fromCustomerToCustomerDto(customer);
    }


    /*
            people
                    .stream()
                    .collect(
            Collectors.teeing(
            Collectors.filtering(PersonService::doesLikeProgramming, Collectors.toList()),
            Collectors.filtering(PersonService::isAdult, Collectors.toList()),
    PersonService::mergePeople
                        )).forEach(System.out::println);*/

    private List<CustomerDto> findCustomersWhoOrderedProductWithSameCountryAsTheir1(){
        return customerOrderRepository.findAll()
                .stream()
                .filter(custOrd -> custOrd.getCustomer().getCountry().getName().equals(custOrd.getProduct().getProducer().getCountry().getName()))
                .map(Mappers::fromCustomerOrderToCustomerOrderDto)
                .map(CustomerOrderDto::getCustomerDto)
                .collect(Collectors.toList());
    }

    private List<CustomerDto> findCustomersWhoOrderedProductWithDifferentCountryAsTheir1(){
        return customerOrderRepository.findAll()
                .stream()
                .filter(custOrd -> !custOrd.getCustomer().getCountry().getName().equals(custOrd.getProduct().getProducer().getCountry().getName()))
                .map(Mappers::fromCustomerOrderToCustomerOrderDto)
                .map(CustomerOrderDto::getCustomerDto)
                .collect(Collectors.toList());
    }

/*    private List<CustomerDto> findCustomersWhoOrderedProductWithSameCountryAsTheir(){
        return customerOrderRepository.findAll()
                .stream()
                .collect(Collectors.teeing)
              }*/

    /*public List<CustomerOrderDto> findCustomersWhoOrderedProductWithSameCountryAsTheir() {

        return customerOrderRepository.findAll()
                .stream()
                .filter(custOrd -> custOrd.getCustomer().getCountry().getName().equals(custOrd.getProduct().getProducer().getCountry().getName()))
                .map(Mappers::fromCustomerOrderToCustomerOrderDto)
                .collect(Collectors.toList());
    }*/
}
