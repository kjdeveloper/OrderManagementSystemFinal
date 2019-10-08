package com.app;

import com.app.exceptions.MyException;
import com.app.model.Country;
import com.app.model.Shop;
import com.app.repository.StockRepository;
import com.app.service.services.StockService;
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

import java.util.List;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TestStockService {

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private StockService stockService;

    @Test
    @DisplayName("Add stock with null product")
    public void test1(){

        var throwable = Assertions.assertThrows(
                MyException.class,
                () -> stockService.addProductToStock(null, "CATEGORY", "SHOP", "COUNTRY", 1)
        );

        Assertions.assertEquals("PRODUCT NAME IS NULL", throwable.getExceptionMessage().getMessage(), "TEST 1 FAILED");
    }

    @Test
    @DisplayName("Add stock with null category")
    public void test2(){

        var throwable = Assertions.assertThrows(
                MyException.class,
                () -> stockService.addProductToStock("PRODUCT", null, "SHOP", "COUNTRY", 1)
        );

        Assertions.assertEquals("CATEGORY NAME IS NULL", throwable.getExceptionMessage().getMessage(), "TEST 2 FAILED");
    }

    @Test
    @DisplayName("Add stock with null shop")
    public void test3(){

        var throwable = Assertions.assertThrows(
                MyException.class,
                () -> stockService.addProductToStock("PRODUCT", "CATEGORY", null, "COUNTRY", 1)
        );

        Assertions.assertEquals("SHOP NAME IS NULL", throwable.getExceptionMessage().getMessage(), "TEST 3 FAILED");
    }

    @Test
    @DisplayName("Add stock with null country")
    public void test4(){

        var throwable = Assertions.assertThrows(
                MyException.class,
                () -> stockService.addProductToStock("PRODUCT", "CATEGORY", "SHOP", null, 1)
        );

        Assertions.assertEquals("COUNTRY NAME IS NULL", throwable.getExceptionMessage().getMessage(), "TEST 4 FAILED");
    }

    @Test
    @DisplayName("Add stock with 0 quantity")
    public void test5(){

        var throwable = Assertions.assertThrows(
                MyException.class,
                () -> stockService.addProductToStock("PRODUCT", "CATEGORY", "SHOP", "COUNTRY", 0)
        );

        Assertions.assertEquals("QUANTITY IS LESS OR EQUAL ZERO", throwable.getExceptionMessage().getMessage(), "TEST 5 FAILED");
    }

    @Test
    @DisplayName("Add stock with quantity less than zero")
    public void test6(){

        var throwable = Assertions.assertThrows(
                MyException.class,
                () -> stockService.addProductToStock("PRODUCT", "CATEGORY", "SHOP", "COUNTRY", -10)
        );

        Assertions.assertEquals("QUANTITY IS LESS OR EQUAL ZERO", throwable.getExceptionMessage().getMessage(), "TEST 6 FAILED");
    }

    /*@Test
    @DisplayName("Find all shops with different country as they product producer")
    public void test7() {

        var country1 = Country.builder().name("POLAND").build();
        var shop1 = Shop.builder().name("LONGI").country(country1).build();

        Mockito.when(stockRepository.findShopWithDifferentCountryThanProductInShop()).thenReturn(List.of(shop1));

        var shops = shopService.findAllShopsWithProductsWithCountryDifferentThanShopCountry();
        Assertions.assertEquals(1, shops.size(), "TEST 7 FAILED");
    }*/
}
