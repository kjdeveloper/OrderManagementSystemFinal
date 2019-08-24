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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class CustomerOrderService {

    private final CustomerOrderRepository customerOrderRepository = new CustomerOrderRepositoryImpl();
    private final StockRepository stockRepository = new StockRepositoryImpl();
    private final ShopRepository shopRepository = new ShopRepositoryImpl();
    private final CustomerRepository customerRepository = new CustomerRepositoryImpl();
    private final ProductRepository productRepository = new ProductRepositoryImpl();

    private final CustomerOrderValidator customerOrderValidator = new CustomerOrderValidator();
    private final ProductValidator productValidator = new ProductValidator();

    public CustomerOrderDto addCustomerOrder(CustomerOrderDto customerOrderDto) {
        customerOrderValidator.validateCustomerOrder(customerOrderDto);

        CustomerOrder customerOrder = Mappers.fromCustomerOrderDtoToCustomerOrder(customerOrderDto);

        CustomerDto customerDto = customerOrderDto.getCustomerDto();

        Customer customer = customerRepository
                .findByName(customerDto.getName())
                .orElseGet(() -> customerRepository
                        .findBySurname(customerDto.getSurname())
                        .orElseThrow(() -> new MyException("CUSTOMER WAS NOT FOUND")));

        ProductDto productDto = customerOrderDto.getProductDto();

        Product product = productRepository
                .findByName(productDto.getName())
                .orElseThrow(() -> new MyException("PRODUCT WAS NOT FOUND"));

        if (stockRepository.countProduct(product.getId()) < customerOrderDto.getQuantity()){
            throw new MyException("Unfortunately, we do not have " + product.getName() + " in quantity " + customerOrderDto.getQuantity());
        }

        //gdzie ustawic set EGuarantees

        customerOrder.setCustomer(customer);
        customerOrder.setProduct(product);
        customerOrderRepository.addOrUpdate(customerOrder);
        return Mappers.fromCustomerOrderToCustomerOrderDto(customerOrder);
    }


    public List<Product> customerOrdersWithSpecificProduct(String product) {
        return customerOrderRepository.findAll()
                .stream()
                .map(CustomerOrder::getProduct)
                .filter(pro -> pro.getName().equals(product))
                .collect(Collectors.toList());
    }

    private BigDecimal productPriceAfterDiscount(CustomerOrder customerOrder) {
        if (customerOrder == null){
            throw new MyException("CUSTOMER ORDER IS NULL");
        }

        BigDecimal price = customerOrder.getProduct().getPrice();
        BigDecimal discount = BigDecimal.valueOf(customerOrder.getDiscount());
        BigDecimal quantity = BigDecimal.valueOf(customerOrder.getQuantity());

        return price.multiply(quantity).multiply(discount);
    }


    public List<CustomerOrderDto> findOrdersBetweenDatesAndGivenPrice(LocalDate dateFrom, LocalDate dateTo, BigDecimal price) {
        if (dateFrom == null) {
            throw new MyException("START DATE CAN NOT BE NULL");
        }
        if (dateTo == null) {
            throw new MyException("FINISH DATE CAN NOT BE NULL");
        }
        if (dateFrom.isAfter(dateTo)) {
            throw new MyException("START DATE CAN NOT BE AFTER FINISH DATE");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new MyException("PRICE CAN NOT BE NULL, LESS OR EQUAL ZERO");
        }

        return customerOrderRepository.findAll()
                .stream()
                .filter(date -> dateFrom.compareTo(date.getDate().toLocalDateTime().toLocalDate()) >= 0 && dateTo.compareTo(date.getDate().toLocalDateTime().toLocalDate()) <= 0)
                .filter(customOrd -> productPriceAfterDiscount(customOrd).compareTo(price) > 0)
                .map(Mappers::fromCustomerOrderToCustomerOrderDto)
                .collect(Collectors.toList());

    }

    public Map<Producer, List<Product>> findProductsByCustomerAndHisCountry(String customerName, String customerSurname, String countryName) {
        return customerOrderRepository.findAll()
                .stream()
                .filter(customer -> customer.getCustomer().getName().equals(customerName) &&
                        customer.getCustomer().getSurname().equals(customerSurname) &&
                        customer.getCustomer().getCountry().getName().equals(countryName))
                .map(CustomerOrder::getProduct)
                .collect(Collectors.groupingBy(Product::getProducer, Collectors.toList()));
    }

}
