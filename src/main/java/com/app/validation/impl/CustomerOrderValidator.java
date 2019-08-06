package com.app.validation.impl;

import com.app.dto.CustomerOrderDto;

import java.time.LocalDateTime;

public class CustomerOrderValidator {

    private boolean isDateValid(CustomerOrderDto customer_orderDto){
        return customer_orderDto.getDate().isAfter(LocalDateTime.now());
    }

    private boolean isDiscountValid(CustomerOrderDto customer_orderDto){
        return customer_orderDto.getDiscount() > 0.0 && customer_orderDto.getDiscount() < 1.0;
    }
}
