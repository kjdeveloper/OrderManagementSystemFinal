package com.app;

import com.app.dto.CountryDto;
import com.app.dto.CustomerDto;
import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.app.model.Country;
import com.app.model.Customer;
import com.app.repository.CustomerRepository;
import com.app.service.services.CustomerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TestCustomerService {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    @DisplayName("Find all customers")
    public void test1() {

        var country = Country.builder().name("POLAND").id(1L).build();
        var country2 = Country.builder().name("ITALY").id(2L).build();

        Mockito.when(customerRepository.findAll()).thenReturn(List.of(
                Customer.builder().name("KAMIL").surname("JANK").age(31).country(country).build(),
                Customer.builder().name("ANN").surname("OLEKU").age(44).country(country2).build(),
                Customer.builder().name("JOHN").surname("ROMONO").age(19).country(country).build()
        ));

        var customers = customerRepository.findAll();

        Assertions.assertEquals(3, customers.size(), "TEST 1 FAILED");
    }

    @Test
    @DisplayName("Add null customer exception")
    public void test2() {

        Mockito.doThrow(new MyException(ExceptionCode.CUSTOMER, "CUSTOMER CANNOT BE NULL"))
                .when(customerRepository).addOrUpdate(ArgumentMatchers.isNull());

        var throwable = Assertions.assertThrows(
                MyException.class,
                () -> customerService.addCustomer(null));

        Assertions.assertEquals("CUSTOMER CANNOT BE NULL", throwable.getExceptionMessage().getMessage(), "TEST 2 FAILED");
    }

    @Test
    @DisplayName("Add customer with wrong name exception")
    public void test3() {
        var countryDto = CountryDto.builder().name("POLAND").id(1L).build();
        var customerDto = CustomerDto.builder().name("Kamil").surname("JANK").age(31).countryDto(countryDto).build();

        var throwable = Assertions.assertThrows(
                MyException.class,
                () -> customerService.addCustomer(customerDto));

        Assertions.assertEquals("INVALID CUSTOMER NAME", throwable.getExceptionMessage().getMessage(), "TEST 3 FAILED");
    }

    @Test
    @DisplayName("Add customer with wrong age exception")
    public void test4(){

        var countryDto = CountryDto.builder().name("POLAND").id(1L).build();
        var customerDto = CustomerDto.builder().name("KAMIL").surname("JANK").age(12).countryDto(countryDto).build();

        var throwable = Assertions.assertThrows(
                MyException.class,
                () -> customerService.addCustomer(customerDto));

        Assertions.assertEquals("CUSTOMER AGE CANNOT BE BELOW 18.", throwable.getExceptionMessage().getMessage(), "TEST 4 FAILED"); }

    @Test
    @DisplayName("Add customer with wrong surname exception")
    public void test5() {
        var countryDto = CountryDto.builder().name("POLAND").id(1L).build();
        var customerDto = CustomerDto.builder().name("KAMIL").surname("JANKow").age(31).countryDto(countryDto).build();

        var throwable = Assertions.assertThrows(
                MyException.class,
                () -> customerService.addCustomer(customerDto));

        Assertions.assertEquals("INVALID CUSTOMER SURNAME", throwable.getExceptionMessage().getMessage(), "TEST 5 FAILED");
    }

}
