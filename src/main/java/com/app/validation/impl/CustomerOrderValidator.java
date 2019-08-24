package com.app.validation.impl;

import com.app.dto.CustomerOrderDto;
import com.app.exceptions.MyException;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CustomerOrderValidator {

    public void validateCustomerOrder(CustomerOrderDto customerOrderDto){
        if (customerOrderDto == null){
            throw new MyException("CUSTOMER ORDER CAN NOT BE NULL");
        }
        if (!isDateValid(customerOrderDto.getDate())){
            throw new MyException("DATE IS NOT VALID");
        }
        if (!isDiscountValid(customerOrderDto.getDiscount())){
            throw new MyException("DISCOUNT VALUE IS NOT VALID");
        }
    }

    private boolean isDateValid(LocalDateTime localDateTime){
        return localDateTime.compareTo(LocalDateTime.now()) <= 0;
    }

    private boolean isDiscountValid(Double discount){
        return discount > 0.0 && discount < 1.0;
    }
}
