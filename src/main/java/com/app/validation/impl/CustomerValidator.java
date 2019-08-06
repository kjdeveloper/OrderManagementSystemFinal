package com.app.validation.impl;

import com.app.dto.CustomerDto;
import com.app.exceptions.MyException;

import static com.mysql.jdbc.StringUtils.isNullOrEmpty;

public class CustomerValidator {

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
