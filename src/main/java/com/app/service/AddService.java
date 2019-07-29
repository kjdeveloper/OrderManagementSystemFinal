package com.app.service;

import com.app.dto.CustomerDto;

public class AddService {

    private final CustomerService customerService = new CustomerService();
    private final ShopService shopService = new ShopService();
    private final ProducerService producerService = new ProducerService();
    private final ProductService productService = new ProductService();
    private final StockService stockService = new StockService();

    public AddService() {
    }

    public CustomerDto addCustomer(CustomerDto customerDto){
        return customerService.addCustomer(customerDto);
    }

}
