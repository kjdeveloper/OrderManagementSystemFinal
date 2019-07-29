package com.app.validation.impl;

import com.app.dto.CustomerDto;
import com.app.exceptions.MyException;
import com.app.validation.generic.AbstractValidator;

import java.util.Map;

import static com.mysql.jdbc.StringUtils.isNullOrEmpty;

public class CustomerValidator extends AbstractValidator<CustomerDto> {

    @Override
    public Map<String, String> validate(CustomerDto customerDTO) {
        errors.clear();

        if (customerDTO == null) {
            errors.put("customerDTO", "null");
        }

        if (!isNameValid(customerDTO)) {
            errors.put("customerDTO name", "Name is not valid => " + customerDTO.getName());
        }
        if (!isSurnameValid(customerDTO)) {
            errors.put("customerDTO surname", "Surname is not valid => " + customerDTO.getSurname());
        }
        if (!isAgeValid(customerDTO)) {
            errors.put("customerDTO age", "Age is not valid => " + customerDTO.getAge());
        }
        return errors;
    }

    public void validateCustomer(final CustomerDto customerDto) {
        if (isNullOrEmpty(customerDto.getName())) {
            throw new MyException("CUSTOMER CANNOT BE NULL OR EMPTY");
        }
        if (!isNameValid(customerDto)) {
            throw new MyException("INVALID CUSTOMER NAME");
        }
        if (!isSurnameValid(customerDto)) {
            throw new MyException("INVALID CUSTOMER SURNAME");
        }
        if (!isAgeValid(customerDto)) {
            throw new MyException("CUSTOMER AGE CANNOT BE BELOW 18.");
        }
    }

    private boolean isNameValid(CustomerDto customerDTO) {
        String MODEL_NAME_REGEX = "[A-Z ]+";
        return customerDTO.getName().matches(MODEL_NAME_REGEX);
    }

    private boolean isSurnameValid(CustomerDto customerDTO) {
        String MODEL_NAME_REGEX = "[A-Z ]+";
        return customerDTO.getSurname().matches(MODEL_NAME_REGEX);
    }

    private boolean isAgeValid(CustomerDto customerDTO) {
        return customerDTO.getAge() >= 18;
    }
}
