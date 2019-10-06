package com.app;

import com.app.dto.CustomerOrderDto;
import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.app.model.*;
import com.app.model.enums.EGuarantee;
import com.app.model.enums.EPayment;
import com.app.repository.CustomerOrderRepository;
import com.app.service.mapper.Mappers;
import com.app.service.services.CustomerOrderService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TestCustomerOrderService {

    @Mock
    private CustomerOrderRepository customerOrderRepository;

    @InjectMocks
    private CustomerOrderService customerOrderService;

    private BigDecimal productPriceAfterDiscount(CustomerOrder customerOrder) {
        if (customerOrder == null) {
            throw new MyException(ExceptionCode.CUSTOMER_ORDER, "CUSTOMER ORDER IS NULL");
        }

        var price = customerOrder.getProduct().getPrice();
        var discountPrice = price.multiply(BigDecimal.valueOf(customerOrder.getDiscount()));
        var quantity = BigDecimal.valueOf(customerOrder.getQuantity());

        return price.subtract(discountPrice).multiply(quantity);
    }

    @Test
    @DisplayName("Find all customer orders between dates and above price")
    public void test1(){

        var country1 = Country.builder().name("POLAND").id(1L).build();
        var country2 = Country.builder().name("ITALY").id(2L).build();
        var country3 = Country.builder().name("SPAIN").id(3L).build();

        var customer1 = Customer.builder().id(4L).name("ROM").surname("ROMANOW").country(country1).age(23).build();
        var customer2 = Customer.builder().id(5L).name("TIM").surname("TIMORAW").country(country2).age(32).build();
        var customer3 = Customer.builder().id(6L).name("LOLEK").surname("LOLUNOW").country(country3).age(44).build();

        var trade1 = Trade.builder().name("CONSTRUCTION").build();
        var trade2 = Trade.builder().name("ECONOMIC").build();

        var producer1 = Producer.builder().id(7L).country(country1).trade(trade1).name("STRONG_COMPANY").build();
        var producer2 = Producer.builder().id(8L).country(country2).trade(trade2).name("WEAK_COMPANY").build();

        var category1 = Category.builder().id(9L).name("CATEGORY_1").build();
        var category2 = Category.builder().id(10L).name("CATEGORY_2").build();

        var product1 = Product.builder()
                .producer(producer1)
                .price(BigDecimal.valueOf(100))
                .name("PRODUCT_1")
                .eGuarantees(new HashSet<>(List.of(EGuarantee.EXCHANGE, EGuarantee.MONEY_BACK)))
                .category(category1)
                .id(11L)
                .build();

        var product2 = Product.builder()
                .producer(producer1)
                .price(BigDecimal.valueOf(50))
                .name("PRODUCT_2")
                .eGuarantees(new HashSet<>(List.of(EGuarantee.EXCHANGE, EGuarantee.MONEY_BACK)))
                .category(category2)
                .id(12L)
                .build();

        var product3 = Product.builder()
                .producer(producer2)
                .price(BigDecimal.valueOf(300))
                .name("{PRODUCT_3}")
                .eGuarantees(new HashSet<>(List.of(EGuarantee.EXCHANGE, EGuarantee.MONEY_BACK)))
                .category(category2)
                .id(13L)
                .build();

        Mockito.when(customerOrderRepository.findAll()).thenReturn(List.of(
                CustomerOrder.builder().product(product1).customer(customer1).quantity(1).discount(0.0).date(LocalDateTime.of(2019,4, 12, 13, 20)).ePayments(new HashSet<>(List.of(EPayment.CARD, EPayment.CASH))).id(14L).build(),
                CustomerOrder.builder().product(product2).customer(customer2).quantity(1).discount(0.0).date(LocalDateTime.of(2019,8, 12, 13, 20)).ePayments(new HashSet<>(List.of(EPayment.CARD, EPayment.CASH))).id(15L).build(),
                CustomerOrder.builder().product(product3).customer(customer3).quantity(1).discount(0.0).date(LocalDateTime.of(2019,9, 23, 13, 20)).ePayments(new HashSet<>(List.of(EPayment.CARD, EPayment.CASH))).id(16L).build()
        ));

        var lcdt1 = LocalDate.of(2019, 3, 12);
        var lcdt2 = LocalDate.of(2019, 9, 30);
        var price = new BigDecimal(50);

        var customerOrders = customerOrderService.findOrdersBetweenDatesAndGivenPrice(lcdt1, lcdt2, price);

        Assertions.assertEquals(2, customerOrders.size(), "TEST 1 FAILED");
    }

    @Test
    @DisplayName("Find all customer orders between dates and above price with wrong start date")
    public void test2(){

        var lcd = LocalDate.now();
        var price = new BigDecimal(100);

        var throwable = Assertions.assertThrows(
                MyException.class,
                () -> customerOrderService.findOrdersBetweenDatesAndGivenPrice(null, lcd, price));

        Assertions.assertEquals("START DATE CANNOT BE NULL", throwable.getExceptionMessage().getMessage(), "TEST 2 FAILED");
    }

    @Test
    @DisplayName("Find all customer orders between dates and above price with wrong start and end date")
    public void test3(){

        var lcd = LocalDate.now().plusYears(2);
        var price = new BigDecimal(100);

        var throwable = Assertions.assertThrows(
                MyException.class,
                () -> customerOrderService.findOrdersBetweenDatesAndGivenPrice(lcd, LocalDate.now(), price));

        Assertions.assertEquals("START DATE CANNOT BE AFTER FINISH DATE", throwable.getExceptionMessage().getMessage(), "TEST 3 FAILED");
    }

    @Test
    @DisplayName("Find all customer orders between dates and above price with wrong end date")
    public void test4(){

        var lcd = LocalDate.now();
        var price = new BigDecimal(100);

        var throwable = Assertions.assertThrows(
                MyException.class,
                () -> customerOrderService.findOrdersBetweenDatesAndGivenPrice(lcd, null, price));

        Assertions.assertEquals("FINISH DATE CANNOT BE NULL", throwable.getExceptionMessage().getMessage(), "TEST 4 FAILED");
    }

    @Test
    @DisplayName("Find all customer orders between dates and above price with wrong price")
    public void test5(){

        var lcd1 = LocalDate.now();
        var lcd2 = LocalDate.now();
        var price = new BigDecimal(0);

        var throwable = Assertions.assertThrows(
                MyException.class,
                () -> customerOrderService.findOrdersBetweenDatesAndGivenPrice(lcd1, lcd2, price));

        Assertions.assertEquals("PRICE CANNOT BE NULL, LESS OR EQUAL ZERO", throwable.getExceptionMessage().getMessage(), "TEST 5 FAILED");
    }

}
