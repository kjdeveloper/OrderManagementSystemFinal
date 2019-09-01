package com.app.service.mapper;

import com.app.dto.*;
import com.app.exceptions.Error;
import com.app.model.*;

import java.sql.Timestamp;
import java.util.HashSet;

public interface Mappers {

    static CategoryDto fromCategoryToCategoryDto(Category category) {
        return category == null ? null : CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .productDtos(new HashSet<>())
                .build();
    }

    static Category fromCategoryDtoToCategory(CategoryDto categoryDTO) {
        return categoryDTO == null ? null : Category.builder()
                .id(categoryDTO.getId())
                .name(categoryDTO.getName())
                .products(new HashSet<>())
                .build();
    }

    static CountryDto fromCountryToCountryDto(Country country) {
        return country == null ? null : CountryDto.builder()
                .id(country.getId())
                .name(country.getName())
                .customerDtos(new HashSet<>())
                .producerDtos(new HashSet<>())
                .shopDtos(new HashSet<>())
                .build();
    }

    static Country fromCountryDtoToCountry(CountryDto countryDto) {
        return countryDto == null ? null : Country.builder()
                .id(countryDto.getId())
                .name(countryDto.getName())
                .customers(new HashSet<>())
                .producers(new HashSet<>())
                .shops(new HashSet<>())
                .build();
    }

    static CustomerDto fromCustomerToCustomerDto(Customer customer) {
        return customer == null ? null : CustomerDto.builder()
                .id(customer.getId())
                .name(customer.getName())
                .surname(customer.getSurname())
                .age(customer.getAge())
                .countryDto(customer.getCountry() == null ? null : fromCountryToCountryDto(customer.getCountry()))
                .customerOrderDtos(new HashSet<>())
                .build();
    }

    static Customer fromCustomerDtoToCustomer(CustomerDto customerDto) {
        return customerDto == null ? null : Customer.builder()
                .id(customerDto.getId())
                .name(customerDto.getName())
                .surname(customerDto.getSurname())
                .age(customerDto.getAge())
                .customerOrders(new HashSet<>())
                .country(customerDto.getCountryDto() == null ? null : fromCountryDtoToCountry(customerDto.getCountryDto()))
                .build();
    }

    static CustomerOrderDto fromCustomerOrderToCustomerOrderDto(CustomerOrder customerOrder) {
        return customerOrder == null ? null : CustomerOrderDto.builder()
                .id(customerOrder.getId())
                .date((customerOrder.getDate().toLocalDateTime()))
                .discount(customerOrder.getDiscount())
                .quantity(customerOrder.getQuantity())
                .customerDto(customerOrder.getCustomer() == null ? null : fromCustomerToCustomerDto(customerOrder.getCustomer()))
                .ePayments(customerOrder.getEPayments())
                .productDto(customerOrder.getProduct() == null ? null : fromProductToProductDto(customerOrder.getProduct()))
                .build();
    }

    static CustomerOrder fromCustomerOrderDtoToCustomerOrder(CustomerOrderDto customerOrderDTO) {
        return customerOrderDTO == null ? null : CustomerOrder.builder()
                .id(customerOrderDTO.getId())
                .date(Timestamp.valueOf(customerOrderDTO.getDate()))
                .discount(customerOrderDTO.getDiscount())
                .quantity(customerOrderDTO.getQuantity())
                .customer(customerOrderDTO.getCustomerDto() == null ? null : fromCustomerDtoToCustomer(customerOrderDTO.getCustomerDto()))
                .ePayments(customerOrderDTO.getEPayments())
                .product(customerOrderDTO.getProductDto() == null ? null : fromProductDtoToProduct(customerOrderDTO.getProductDto()))
                .build();
    }

    static ProducerDto fromProducerToProducerDto(Producer producer) {
        return producer == null ? null : ProducerDto.builder()
                .id(producer.getId())
                .name(producer.getName())
                .countryDto(producer.getCountry() == null ? null : fromCountryToCountryDto(producer.getCountry()))
                .productsDtos(new HashSet<>())
                .tradeDto(producer.getTrade() == null ? null : fromTradeToTradeDto(producer.getTrade()))
                .build();
    }

    static Producer fromProducerDtoToProducer(ProducerDto producerDto) {
        return producerDto == null ? null : Producer.builder()
                .id(producerDto.getId())
                .name(producerDto.getName())
                .country(producerDto.getCountryDto() == null ? null : fromCountryDtoToCountry(producerDto.getCountryDto()))
                .products(new HashSet<>())
                .trade(producerDto.getTradeDto() == null ? null : fromTradeDtoToTrade(producerDto.getTradeDto()))
                .build();
    }

    static ProductDto fromProductToProductDto(Product product) {
        return product == null ? null : ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .customerOrderDtos(new HashSet<>())
                .eGuarantees(product.getEGuarantees())
                .stockDtos(new HashSet<>())
                .categoryDto(product.getCategory() == null ? null : fromCategoryToCategoryDto(product.getCategory()))
                .producerDto(product.getProducer() == null ? null : fromProducerToProducerDto(product.getProducer()))
                .build();
    }

    static Product fromProductDtoToProduct(ProductDto productDto) {
        return productDto == null ? null : Product.builder()
                .id(productDto.getId())
                .name(productDto.getName())
                .price(productDto.getPrice())
                .customerOrders(new HashSet<>())
                .eGuarantees(productDto.getEGuarantees())
                .stocks(new HashSet<>())
                .category(productDto.getCategoryDto() == null ? null : fromCategoryDtoToCategory(productDto.getCategoryDto()))
                .producer(productDto.getProducerDto() == null ? null : fromProducerDtoToProducer(productDto.getProducerDto()))
                .build();
    }

    static ShopDto fromShopToShopDto(Shop shop) {
        return shop == null ? null : ShopDto.builder()
                .id(shop.getId())
                .name(shop.getName())
                .countryDto(shop.getCountry() == null ? null : fromCountryToCountryDto(shop.getCountry()))
                .stockDtos(new HashSet<>())
                .build();
    }

    static Shop fromShopDtoToShop(ShopDto shopDto) {
        return shopDto == null ? null : Shop.builder()
                .id(shopDto.getId())
                .name(shopDto.getName())
                .stocks(new HashSet<>())
                .country(shopDto.getCountryDto() == null ? null : fromCountryDtoToCountry(shopDto.getCountryDto()))
                .build();
    }

    static StockDto fromStockToStockDto(Stock stock) {
        return stock == null ? null : StockDto.builder()
                .id(stock.getId())
                .quantity(stock.getQuantity())
                .productDto(stock.getProduct() == null ? null : fromProductToProductDto(stock.getProduct()))
                .shopDto(stock.getShop() == null ? null : fromShopToShopDto(stock.getShop()))
                .build();
    }

    static Stock fromStockDtoToStock(StockDto stockDto) {
        return stockDto == null ? null : Stock.builder()
                .id(stockDto.getId())
                .quantity(stockDto.getQuantity())
                .product(stockDto.getProductDto() == null ? null : fromProductDtoToProduct(stockDto.getProductDto()))
                .shop(stockDto.getShopDto() == null ? null : fromShopDtoToShop(stockDto.getShopDto()))
                .build();
    }

    static TradeDto fromTradeToTradeDto(Trade trade) {
        return trade == null ? null : TradeDto.builder()
                .id(trade.getId())
                .name(trade.getName())
                .producerDtos(new HashSet<>())
                .build();
    }

    static Trade fromTradeDtoToTrade(TradeDto tradeDto) {
        return tradeDto == null ? null : Trade.builder()
                .id(tradeDto.getId())
                .name(tradeDto.getName())
                .producers(new HashSet<>())
                .build();
    }

    static ErrorDto fromErrorToErrorDto(Error error) {
        return error == null ? null : ErrorDto.builder()
                .id(error.getId())
                .date(error.getDate().toLocalDateTime())
                .message(error.getMessage())
                .build();
    }

    static Error fromErrorDtoToError(ErrorDto errorDto) {
        return errorDto == null ? null : Error.builder()
                .id(errorDto.getId())
                .date(Timestamp.valueOf(errorDto.getDate()))
                .message(errorDto.getMessage())
                .build();

    }
}
