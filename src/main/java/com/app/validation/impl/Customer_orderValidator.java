package com.app.validation.impl;

import com.app.dto.Customer_orderDto;
import com.app.exceptions.MyException;
import com.app.validation.generic.AbstractValidator;

import java.time.LocalDateTime;
import java.util.Map;

public class Customer_orderValidator extends AbstractValidator<Customer_orderDto> {

    @Override
    public Map<String, String> validate(Customer_orderDto customer_orderDTO) {
        return null;
    }

    public void validateCustomerOrder(Customer_orderDto customer_orderDto){
        if (customer_orderDto == null){
            throw new MyException("CUSTOEMR ORDER CANNOT BE NULL");
        }
        if (!isDateValid(customer_orderDto)){
            throw new MyException("INVALID DATE IN CUSTOMER ORDER");
        }
        if (!isDicsountValid(customer_orderDto)){
            throw  new MyException("INVALID DISCOUNT IN CUSTOMER ORDER");
        }
    }

    private boolean isDateValid(Customer_orderDto customer_orderDto){
        return customer_orderDto.getDate().isAfter(LocalDateTime.now());
    }

    private boolean isDicsountValid(Customer_orderDto customer_orderDto){
        return customer_orderDto.getDiscount() > 0.0 && customer_orderDto.getDiscount() < 1.0;
    }
}
