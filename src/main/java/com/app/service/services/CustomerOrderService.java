package com.app.service.services;

import com.app.dto.CustomerDto;
import com.app.dto.CustomerOrderDto;
import com.app.dto.ProducerDto;
import com.app.dto.ProductDto;
import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.app.model.Customer;
import com.app.model.CustomerOrder;
import com.app.model.Product;
import com.app.model.Stock;
import com.app.repository.CustomerOrderRepository;
import com.app.repository.CustomerRepository;
import com.app.repository.ProductRepository;
import com.app.repository.StockRepository;
import com.app.repository.impl.CustomerOrderRepositoryImpl;
import com.app.repository.impl.CustomerRepositoryImpl;
import com.app.repository.impl.ProductRepositoryImpl;
import com.app.repository.impl.StockRepositoryImpl;
import com.app.service.mapper.Mappers;
import com.app.validation.impl.CustomerOrderValidator;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomerOrderService {

    private final CustomerOrderRepository customerOrderRepository = new CustomerOrderRepositoryImpl();
    private final StockRepository stockRepository = new StockRepositoryImpl();
    private final CustomerRepository customerRepository = new CustomerRepositoryImpl();
    private final ProductRepository productRepository = new ProductRepositoryImpl();

    private final CustomerOrderValidator customerOrderValidator = new CustomerOrderValidator();

    public CustomerOrderDto addCustomerOrder(CustomerOrderDto customerOrderDto) {
        customerOrderValidator.validateCustomerOrder(customerOrderDto);

        CustomerOrder customerOrder = Mappers.fromCustomerOrderDtoToCustomerOrder(customerOrderDto);

        CustomerDto customerDto = customerOrderDto.getCustomerDto();

        Customer customer = customerRepository
                .findByName(customerDto.getName())
                .orElseGet(() -> customerRepository
                        .findBySurname(customerDto.getSurname())
                        .orElseThrow(() -> new MyException(ExceptionCode.CUSTOMER, "CUSTOMER WAS NOT FOUND. PLEASE ADD CUSTOMER FIRST")));

        ProductDto productDto = customerOrderDto.getProductDto();

        Product product = productRepository
                .findByName(productDto.getName())
                .orElseThrow(() -> new MyException(ExceptionCode.PRODUCT, "PRODUCT WAS NOT FOUND. PLEASE ADD PRODUCT FIRST"));

        int countProduct = stockRepository.countProduct(product.getId());
        System.out.println(countProduct);

        if (countProduct < customerOrderDto.getQuantity()) {
            throw new MyException(ExceptionCode.PRODUCT, "UNFORTUNATELY, WE DO NOT HAVE " + product.getName() + " IN AN EQUAL AMOUNT " + customerOrderDto.getQuantity());
        }

        List<Stock> stocksWithSpecificProductId = stockRepository.findStocksWithSpecificProduct(product.getId())
                .stream()
                .sorted(Comparator.comparing(Stock::getQuantity).reversed())
                .collect(Collectors.toList());

        int givenValueOfProductToBuy = customerOrderDto.getQuantity();

        if (stocksWithSpecificProductId.get(0).getQuantity() - givenValueOfProductToBuy < 0) {
            Stock stock1 = stocksWithSpecificProductId.get(0);
            int quantityFromAnotherShop = givenValueOfProductToBuy - stocksWithSpecificProductId.get(0).getQuantity();
            Stock stock2 = stocksWithSpecificProductId.get(1);
            stock1.setQuantity(0);
            stockRepository.addOrUpdate(stock1);
            stock2.setQuantity(stock2.getQuantity() - quantityFromAnotherShop);
            stockRepository.addOrUpdate(stock2);
        } else {
            Stock stock1 = stocksWithSpecificProductId.get(0);
            stock1.setQuantity(stock1.getQuantity() - givenValueOfProductToBuy);
            stockRepository.addOrUpdate(stock1);
        }
        customerOrder.setCustomer(customer);
        customerOrder.setProduct(product);
        customerOrderRepository.addOrUpdate(customerOrder);
        return Mappers.fromCustomerOrderToCustomerOrderDto(customerOrder);
    }

    private BigDecimal productPriceAfterDiscount(CustomerOrder customerOrder) {
        if (customerOrder == null) {
            throw new MyException(ExceptionCode.CUSTOMER_ORDER, "CUSTOMER ORDER IS NULL");
        }

        BigDecimal price = customerOrder.getProduct().getPrice();
        BigDecimal discountPrice = price.multiply(BigDecimal.valueOf(customerOrder.getDiscount()));
        BigDecimal quantity = BigDecimal.valueOf(customerOrder.getQuantity());

        return price.subtract(discountPrice).multiply(quantity);
    }


    public List<CustomerOrderDto> findOrdersBetweenDatesAndGivenPrice(LocalDate customerDateFrom, LocalDate customerDateTo, BigDecimal price) {
        if (customerDateFrom == null) {
            throw new MyException(ExceptionCode.CUSTOMER_ORDER, "START DATE CAN NOT BE NULL");
        }
        if (customerDateTo == null) {
            throw new MyException(ExceptionCode.CUSTOMER_ORDER, "FINISH DATE CAN NOT BE NULL");
        }
        if (customerDateFrom.compareTo(customerDateTo) > 0) {
            throw new MyException(ExceptionCode.CUSTOMER_ORDER, "START DATE CAN NOT BE AFTER FINISH DATE");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new MyException(ExceptionCode.CUSTOMER_ORDER, "PRICE CAN NOT BE NULL, LESS OR EQUAL ZERO");
        }

        Date dateFrom = Date.valueOf(customerDateFrom);
        Date dateTo = Date.valueOf(customerDateTo);

        return customerOrderRepository.findAll()
                .stream()
                .filter(order -> dateFrom.compareTo(order.getDate()) <= 0 && dateTo.compareTo(order.getDate()) >= 0)
                .peek(s -> System.out.println(s))
                .filter(order1 -> productPriceAfterDiscount(order1).compareTo(price) > 0)
                .map(Mappers::fromCustomerOrderToCustomerOrderDto)
                .collect(Collectors.toList());

    }

    public Map<ProducerDto, List<ProductDto>> findProductsByCustomerAndHisCountry(String customerName, String customerSurname, String countryName) {
        if (customerName == null) {
            throw new MyException(ExceptionCode.CUSTOMER_ORDER, "CUSTOMER NAME CAN NOT BE NULL");
        }
        if (customerSurname == null) {
            throw new MyException(ExceptionCode.CUSTOMER_ORDER, "CUSTOMER SURNAME CAN NOT BE NULL");
        }
        if (countryName == null) {
            throw new MyException(ExceptionCode.CUSTOMER_ORDER, "COUNTRY NAME CAN NOT BE NULL");
        }
        return customerOrderRepository.findProductsByCustomerAndHisCountry(customerName, customerSurname, countryName)
                .stream()
                .map(CustomerOrder::getProduct)
                .map(Mappers::fromProductToProductDto)
                .collect(Collectors.groupingBy(ProductDto::getProducerDto));
    }

    public Set<CustomerDto> findCustomersWhoOrderedProductWithSameCountryAsTheir() {
        return customerOrderRepository.findCustomersWhoOrderedProductWithSameCountryAsTheir()
                .stream()
                .map(Mappers::fromCustomerToCustomerDto)
                .collect(Collectors.toSet());
    }

}
