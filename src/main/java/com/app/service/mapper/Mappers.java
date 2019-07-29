package com.app.service.mapper;

import com.app.dto.*;
import com.app.exceptions.Error;
import com.app.model.*;

import java.sql.Timestamp;
import java.util.HashSet;

public interface Mappers {

    static CategoryDto fromCategoryToCategoryDTO(Category category) {
        return category == null ? null : CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .productDtos(new HashSet<>())
                .build();
    }

    static Category fromCategoryDTOtoCategory(CategoryDto categoryDTO) {
        return categoryDTO == null ? null : Category.builder()
                .id(categoryDTO.getId())
                .name(categoryDTO.getName())
                .products(new HashSet<>())
                .build();
    }

    static CountryDto fromCountryToCountryDTO(Country country) {
        return country == null ? null : CountryDto.builder()
                .id(country.getId())
                .name(country.getName())
                .customerDtos(new HashSet<>())
                .producerDtos(new HashSet<>())
                .shopDtos(new HashSet<>())
                .build();
    }

    static Country fromCountryDTOToCountry(CountryDto countryDTO) {
        return countryDTO == null ? null : Country.builder()
                .id(countryDTO.getId())
                .name(countryDTO.getName())
                .customers(new HashSet<>())
                .producers(new HashSet<>())
                .shops(new HashSet<>())
                .build();
    }

    static CustomerDto fromCustomerToCustomerDTO(Customer customer) {
        return customer == null ? null : CustomerDto.builder()
                .id(customer.getId())
                .name(customer.getName())
                .surname(customer.getSurname())
                .age(customer.getAge())
                .countryDTO(customer.getCountry() == null ? null : fromCountryToCountryDTO(customer.getCountry()))
                .customerOrderDTOS(new HashSet<>())
                .build();
    }

    static Customer fromCustomerDTOToCustomer(CustomerDto customerDTO) {
        return customerDTO == null ? null : Customer.builder()
                .id(customerDTO.getId())
                .name(customerDTO.getName())
                .surname(customerDTO.getSurname())
                .age(customerDTO.getAge())
                .customerOrders(new HashSet<>())
                .country(customerDTO.getCountryDTO() == null ? null : fromCountryDTOToCountry(customerDTO.getCountryDTO()))
                .build();
    }

    static Customer_orderDto fromCustomer_orderToCustomer_orderDTO(Customer_order customer_order) {
        return customer_order == null ? null : Customer_orderDto.builder()
                .id(customer_order.getId())
                .date((customer_order.getDate().toLocalDateTime()))
                .discount(customer_order.getDiscount())
                .quantity(customer_order.getQuantity())
                .customerDTO(customer_order.getCustomer() == null ? null : fromCustomerToCustomerDTO(customer_order.getCustomer()))
                .ePayments(new HashSet<>())
                .productDTO(customer_order.getProduct() == null ? null : fromProductToProductDTO(customer_order.getProduct()))
                .build();
    }

    static Customer_order fromCustomer_orderDTOToCustomer_order(Customer_orderDto customer_orderDTO) {
        return customer_orderDTO == null ? null : Customer_order.builder()
                .id(customer_orderDTO.getId())
                .date(Timestamp.valueOf(customer_orderDTO.getDate()))
                .discount(customer_orderDTO.getDiscount())
                .quantity(customer_orderDTO.getQuantity())
                .customer(customer_orderDTO.getCustomerDTO() == null ? null : fromCustomerDTOToCustomer(customer_orderDTO.getCustomerDTO()))
                .ePayments(new HashSet<>())
                .product(customer_orderDTO.getProductDTO() == null ? null : fromProductDTOToProduct(customer_orderDTO.getProductDTO()))
                .build();
    }
/*
    static Guarantee_componentsDto fromGuarantee_componentsToGuarantee_componentsDTO(Guarantee_components guarantee_components) {
        return guarantee_components == null ? null : Guarantee_componentsDto.builder()
                .id(guarantee_components.getId())
                .eGuarantees(new HashSet<>())
                .productDTO(guarantee_components.getProduct() == null ? null : fromProductToProductDTO(guarantee_components.getProduct()))
                .build();
    }

    static Guarantee_components fromGuarantee_componentsDTOToGuarantee_componenets(Guarantee_componentsDto guarantee_componentsDTO) {
        return guarantee_componentsDTO == null ? null : Guarantee_components.builder()
                .id(guarantee_componentsDTO.getId())
                .guarantees(new HashSet<>())
                .product(guarantee_componentsDTO.getProductDTO() == null ? null : fromProductDTOToProduct(guarantee_componentsDTO.getProductDTO()))
                .build();
    }*/

    /*static PaymentDto fromPaymentToPaymenrDTO(Payment payment) {
        return payment == null ? null : PaymentDto.builder()
                .id(payment.getId())
                .ePayment(payment.getPayment())
                .customerOrderDTOS(new HashSet<>())
                .build();
    }

    static Payment fromPaymentDTOToPayment(PaymentDto paymentDTO) {
        return paymentDTO == null ? null : Payment.builder()
                .id(paymentDTO.getId())
                .payment(paymentDTO.getEPayment())
                .customer_orders(new HashSet<>())
                .build();
    }*/

    static ProducerDto fromProducerToProducerDTO(Producer producer) {
        return producer == null ? null : ProducerDto.builder()
                .id(producer.getId())
                .name(producer.getName())
                .countryDTO(producer.getCountry() == null ? null : fromCountryToCountryDTO(producer.getCountry()))
                .productsDTOS(new HashSet<>())
                .tradeDTO(producer.getTrade() == null ? null : fromTradeToTradeDTO(producer.getTrade()))
                .build();
    }

    static Producer fromProducerDTOToProducer(ProducerDto producerDTO) {
        return producerDTO == null ? null : Producer.builder()
                .id(producerDTO.getId())
                .name(producerDTO.getName())
                .country(producerDTO.getCountryDTO() == null ? null : fromCountryDTOToCountry(producerDTO.getCountryDTO()))
                .products(new HashSet<>())
                .trade(producerDTO.getTradeDTO() == null ? null : fromTradeDTOToTrade(producerDTO.getTradeDTO()))
                .build();
    }

    static ProductDto fromProductToProductDTO(Product product) {
        return product == null ? null : ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .customerOrderDTOS(new HashSet<>())
                .eGuarantees(new HashSet<>())
                .stockDtos(new HashSet<>())
                .categoryDTO(product.getCategory() == null ? null : fromCategoryToCategoryDTO(product.getCategory()))
                .producerDTO(product.getProducer() == null ? null : fromProducerToProducerDTO(product.getProducer()))
                .build();
    }

    static Product fromProductDTOToProduct(ProductDto productDTO) {
        return productDTO == null ? null : Product.builder()
                .id(productDTO.getId())
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .customer_orders(new HashSet<>())
                .eGuarantees(new HashSet<>())
                .stocks(new HashSet<>())
                .category(productDTO.getCategoryDTO() == null ? null : fromCategoryDTOtoCategory(productDTO.getCategoryDTO()))
                .producer(productDTO.getProducerDTO() == null ? null : fromProducerDTOToProducer(productDTO.getProducerDTO()))
                .build();
    }

    static ShopDto fromShopToShopDTO(Shop shop) {
        return shop == null ? null : ShopDto.builder()
                .id(shop.getId())
                .name(shop.getName())
                .countryDTO(shop.getCountry() == null ? null : fromCountryToCountryDTO(shop.getCountry()))
                .stockDtos(new HashSet<>())
                .build();
    }

    static Shop fromShopDTOToShop(ShopDto shopDTO) {
        return shopDTO == null ? null : Shop.builder()
                .id(shopDTO.getId())
                .name(shopDTO.getName())
                .stocks(new HashSet<>())
                .country(shopDTO.getCountryDTO() == null ? null : fromCountryDTOToCountry(shopDTO.getCountryDTO()))
                .build();
    }

    static StockDto fromStockToStockDTO(Stock stock) {
        return stock == null ? null : StockDto.builder()
                .id(stock.getId())
                .quantity(stock.getQuantity())
                .productDTO(stock.getProduct() == null ? null : fromProductToProductDTO(stock.getProduct()))
                .shopDTO(stock.getShop() == null ? null : fromShopToShopDTO(stock.getShop()))
                .build();
    }

    static Stock fromStockDTOToStock(StockDto stockDTO) {
        return stockDTO == null ? null : Stock.builder()
                .id(stockDTO.getId())
                .quantity(stockDTO.getQuantity())
                .product(stockDTO.getProductDTO() == null ? null : fromProductDTOToProduct(stockDTO.getProductDTO()))
                .shop(stockDTO.getShopDTO() == null ? null : fromShopDTOToShop(stockDTO.getShopDTO()))
                .build();
    }

    static TradeDto fromTradeToTradeDTO(Trade trade) {
        return trade == null ? null : TradeDto.builder()
                .id(trade.getId())
                .name(trade.getName())
                .producerDtos(new HashSet<>())
                .build();
    }

    static Trade fromTradeDTOToTrade(TradeDto tradeDTO) {
        return tradeDTO == null ? null : Trade.builder()
                .id(tradeDTO.getId())
                .name(tradeDTO.getName())
                .producers(new HashSet<>())
                .build();
    }

    static ErrorDto fromErrorToErrorDTO(Error error) {
        return error == null ? null : ErrorDto.builder()
                .id(error.getId())
                .date(error.getDate())
                .message(error.getMessage())
                .build();
    }

    static Error fromErrorDTOToError(ErrorDto errorDTO) {
        return errorDTO == null ? null : Error.builder()
                .id(errorDTO.getId())
                .date(errorDTO.getDate())
                .message(errorDTO.getMessage())
                .build();

    }
}
