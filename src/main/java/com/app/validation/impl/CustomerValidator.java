package com.app.validation.impl;

import com.app.dto.CustomerDto;
import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;

public class CustomerValidator {

    public void validateCustomer(final CustomerDto customerDto) {
        if (customerDto == null) {
            throw new MyException(ExceptionCode.CUSTOMER, "CUSTOMER CANNOT BE NULL");
        }
        if (!isNameValid(customerDto)) {
            throw new MyException(ExceptionCode.CUSTOMER, "INVALID CUSTOMER NAME");
        }
        if (!isSurnameValid(customerDto)) {
            throw new MyException(ExceptionCode.CUSTOMER, "INVALID CUSTOMER SURNAME");
        }
        if (!isAgeValid(customerDto)) {
            throw new MyException(ExceptionCode.CUSTOMER, "CUSTOMER AGE CANNOT BE BELOW 18.");
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
