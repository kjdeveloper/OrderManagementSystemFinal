package com.app;

import com.app.dto.ShopDto;
import com.app.dto.StockDto;
import com.app.model.*;
import com.app.repository.StockRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TestStockService {

    @Mock
    private StockRepository stockRepository;

    @Test
    @DisplayName("Find all stocks")
    public void test1(){

        var shop1 = Shop.builder().name("LONGI").build();
        var shop2 = Shop.builder().name("ARONY").build();

        var country1 = Country.builder().name("POLAND").build();
        var country2 = Country.builder().name("ITALY").build();

        var producer1 = Producer.builder().country(country1).build();
        var producer2 = Producer.builder().country(country2).build();

        var product1 = Product.builder().name("LONOGRYJKI").price(BigDecimal.valueOf(100)).producer(producer1).build();
        var product2 = Product.builder().name("ARONYJKI").price(BigDecimal.valueOf(250)).producer(producer2).build();

        Mockito.when(stockRepository.findAll()).thenReturn(List.of(
                Stock.builder().id(1L).product(product1).shop(shop1).build(),
                Stock.builder().id(2L).product(product2).shop(shop2).build()
        ));

        List<Stock> stocks = stockRepository.findAll();

        Assertions.assertEquals(2, stocks.size(), "TEST 1 FAILED");
    }

 /*   @Test
    @DisplayName("Find all shops which on stock have products with different country as they")
    public void test2(){

        var shop1 = Shop.builder().name("LONGI").build();
        var shop2 = Shop.builder().name("ARONY").build();

        var country1 = Country.builder().name("POLAND").build();
        var country2 = Country.builder().name("ITALY").build();

        var producer1 = Producer.builder().country(country1).build();
        var producer2 = Producer.builder().country(country2).build();

        var product1 = Product.builder().name("LONOGRYJKI").price(BigDecimal.valueOf(100)).producer(producer1).build();
        var product2 = Product.builder().name("ARONYJKI").price(BigDecimal.valueOf(250)).producer(producer2).build();

        Mockito.when(stockRepository.findAll()).thenReturn(List.of(
                Stock.builder().id(1L).product(product1).shop(shop1).build(),
                Stock.builder().id(2L).product(product2).shop(shop2).build()
        ));

        var shops = stockRepository.findShopWithDifferentCountryThanProductInShop();

        Assertions.assertEquals(2, shops.size(), "TEST 1 FAILED");
    }*/
}
