package com.app.service;

import com.app.dto.*;
import com.app.exceptions.MyException;
import com.app.model.*;
import com.app.repository.*;
import com.app.repository.impl.*;
import com.app.service.mapper.Mappers;
import com.app.validation.impl.CustomerValidator;
import com.app.validation.impl.Customer_orderValidator;
import com.app.validation.impl.ProductValidator;

public class CustomerOrderService {

    private final CustomerOrderRepository customerOrderRepository = new CustomerOrderRepositoryImpl();
    private final StockRepository stockRepository = new StockRepositoryImpl();
    private final ShopRepository shopRepository = new ShopRepositoryImpl();
    private final CustomerRepository customerRepository = new CustomerRepositoryImpl();
    private final ProductRepository productRepository = new ProductRepositoryImpl();

    private final Customer_orderValidator customer_orderValidator = new Customer_orderValidator();
    private final CustomerValidator customerValidator = new CustomerValidator();
    private final ProductValidator productValidator = new ProductValidator();


    public void addCustomerOrder(Customer_orderDto customer_orderDto, ShopDto shopDto) {
        customer_orderValidator.validateCustomerOrder(customer_orderDto);
        customerValidator.validateCustomer(customer_orderDto.getCustomerDto());
        productValidator.validateProduct(customer_orderDto.getProductDto());

        Stock stock = returnStockIfExist(customer_orderDto, shopDto);
        Customer customer = returnTheCustomerIfExist(customer_orderDto.getCustomerDto());
        Product product = returnTheProductIfExist(customer_orderDto.getProductDto());
        Shop shop = returnShopIfExist(shopDto);
        int quantityToOrder = customer_orderDto.getQuantity();

        int quantityOnStock = stock.getQuantity();

        if (quantityToOrder > quantityOnStock) {
            throw new MyException(customer_orderDto.getCustomerDto().getName() + " YOU CAN NOT ORDER QUANTITY WHAT YOU WANT. ON STOCK WE HAVE " + quantityOnStock + " PIECES");
        } else {
            stock.setQuantity(quantityOnStock - quantityToOrder);
        }

        Customer_order customer_order = Mappers.fromCustomer_orderDTOToCustomer_order(customer_orderDto);
        customer_order.setCustomer(customer);
        customer_order.setProduct(product);
        customerOrderRepository.addOrUpdate(customer_order);
    }

    private Customer returnTheCustomerIfExist(CustomerDto customerDto) {
        Customer customer = customerRepository.findByName(customerDto.getName()).orElse(null);
        if (customer == null) {
            customer = customerRepository.findBySurname(customerDto.getSurname()).orElse(null);
            if (customer == null) {
                customer = Mappers.fromCustomerDTOToCustomer(customerDto);
                customer = customerRepository.addOrUpdate(customer).orElseThrow(() -> new MyException("CAN NOT ADD CUSTOMER IN CUSTOMER ORDER SERVICE"));
            }
        }
        return customer;
    }

    private Product returnTheProductIfExist(ProductDto productDto) {
        Product product = productRepository.findByName(productDto.getName()).orElse(null);
        if (product == null) {
            product = Mappers.fromProductDTOToProduct(productDto);
            product = productRepository.addOrUpdate(product).orElseThrow(() -> new MyException("CAN NOT ADD PRODUCT IN CUSTOMER ORDER SERVICE"));
        }
        return product;
    }

    private Shop returnShopIfExist(ShopDto shopDto) {
        Shop shop = shopRepository.findByName(shopDto).orElse(null);
        if (shop == null) {
            shop = Mappers.fromShopDTOToShop(shopDto);
            shopRepository.addOrUpdate(shop).orElseThrow(() -> new MyException("CAN NOT ADD SHOP IN ORDER CUSTOMER SERVICE"));
        }
        return shop;
    }

    private Stock returnStockIfExist(Customer_orderDto customer_orderDto, ShopDto shopDto){
        ProductDto productDto = customer_orderDto.getProductDto();

        StockDto stockDto = StockDto.builder()
                .productDTO(productDto)
                .shopDTO(shopDto)
                .build();

        Stock stock = stockRepository.findStockByProductAndShop(stockDto).orElse(null);
        if (stock == null) {
            stock = Mappers.fromStockDTOToStock(stockDto);
            stock = stockRepository.addOrUpdate(stock).orElseThrow(() -> new MyException("CAN NOT ADD STOCK IN CSTOMER ORDER SERVICE"));
        }

        return stock;
    }
}
