package com.app.service.services;

import com.app.dto.*;
import com.app.exceptions.MyException;
import com.app.model.*;
import com.app.repository.*;
import com.app.repository.impl.*;
import com.app.service.mapper.Mappers;
import com.app.validation.impl.CustomerValidator;
import com.app.validation.impl.CustomerOrderValidator;
import com.app.validation.impl.ProductValidator;

public class CustomerOrderService {

    private final CustomerOrderRepository customerOrderRepository = new CustomerOrderRepositoryImpl();
    private final StockRepository stockRepository = new StockRepositoryImpl();
    private final ShopRepository shopRepository = new ShopRepositoryImpl();
    private final CustomerRepository customerRepository = new CustomerRepositoryImpl();
    private final ProductRepository productRepository = new ProductRepositoryImpl();

    private final CustomerOrderValidator customerOrderValidator = new CustomerOrderValidator();
    private final CustomerValidator customerValidator = new CustomerValidator();
    private final ProductValidator productValidator = new ProductValidator();


    public void addCustomerOrder(CustomerOrderDto customerOrderDto) {
    }

    private boolean isProductInStock(Long productId, Integer expectedQuantity) {

        return stockRepository.countProduct(productId) >= expectedQuantity;

    }
}
