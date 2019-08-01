package com.app.service;

import com.app.dto.CustomerDto;
import com.app.dto.ProducerDto;
import com.app.dto.ProductDto;
import com.app.dto.ShopDto;
import com.app.model.Shop;

public class AddService {

    private final CustomerService customerService = new CustomerService();
    private final ShopService shopService = new ShopService();
    private final ProducerService producerService = new ProducerService();
    private final ProductService productService = new ProductService();
    private final StockService stockService = new StockService();

    public AddService() {
    }

    public CustomerDto addCustomer(CustomerDto customerDto) {
        return customerService.addCustomer(customerDto);
    }

    public ShopDto addShop(ShopDto shopDto) {
        return shopService.addShop(shopDto);
    }

    public ProducerDto addProducer(ProducerDto producerDto) {
        return producerService.addProducer(producerDto);
    }


    public ProductDto addProduct(ProductDto productDto) {
        return productService.addProduct(productDto);
    }
}
