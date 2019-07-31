package com.app;

import com.app.dto.*;
import com.app.mainmenu.menu.MenuService;
import com.app.model.enums.EGuarantee;
import com.app.model.enums.EPayment;
import com.app.service.CustomerOrderService;
import com.app.service.ProductService;
import com.app.service.StockService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class App {
    public static void main(String[] args) {

        MenuService menuService = new MenuService();
        //menuService.service();

        CountryDto countryDto = CountryDto.builder()
                .name("PORTUGAL")
                .build();

        CustomerDto customerDto = CustomerDto.builder()
                .name("Fara")
                .surname("BLALOR")
                .age(34)
                .countryDTO(countryDto)
                .build();

        CategoryDto categoryDto = CategoryDto.builder()
                .name("NIEWIEM_CO_TO_zA_KATEGORIA")
                .build();

        TradeDto tradeDto = TradeDto.builder()
                .name("BRANZA_LOL")
                .build();

        ProducerDto producerDto = ProducerDto.builder()
                .countryDTO(countryDto)
                .name("PARALIZATOR_NAME")
                .tradeDTO(tradeDto)
                .build();

        ProductDto productDto = ProductDto.builder()
                .name("PARASOL")
                .categoryDTO(categoryDto)
                .price(new BigDecimal(150))
                .producerDTO(producerDto)
                .eGuarantees(new HashSet<>(Arrays.asList(EGuarantee.EXCHANGE, EGuarantee.MONEY_BACK)))
                .build();

        Customer_orderDto customer_orderDto = Customer_orderDto.builder()
                .customerDto(customerDto)
                .productDto(productDto)
                .ePayments(new HashSet<>(Arrays.asList(EPayment.CARD, EPayment.CASH)))
                .discount(0.5)
                .quantity(2)
                .date(LocalDateTime.now().plusDays(2))
                .build();

        ShopDto shopDto = ShopDto.builder()
                .countryDTO(countryDto)
                .name("ABRAKADABRA")
                .build();

        StockService stockService = new StockService();
        //stockService.addProductToStock(productDto, shopDto, 20);

        CustomerOrderService customerOrderService = new CustomerOrderService();
        customerOrderService.addCustomerOrder(customer_orderDto, shopDto);

    }
}
