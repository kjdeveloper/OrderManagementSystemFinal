package com.app.service;

import com.app.dto.Customer_orderDto;
import com.app.repository.CustomerOrderRepository;
import com.app.repository.impl.CustomerOrderRepositoryImpl;
import com.app.validation.impl.Customer_orderValidator;

public class CustomerOrderService {

    private final CustomerOrderRepository customerOrderRepository = new CustomerOrderRepositoryImpl();
    private final Customer_orderValidator customer_orderValidator = new Customer_orderValidator();

    public void addCustomerOrder(Customer_orderDto customer_orderDto){
        customer_orderValidator.validateCustomerOrder(customer_orderDto);





    }



}
