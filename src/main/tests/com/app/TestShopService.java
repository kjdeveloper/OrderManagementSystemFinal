package com.app;

import com.app.dto.ShopDto;
import com.app.exceptions.MyException;
import com.app.service.services.ShopService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TestShopService {

    @InjectMocks
    private ShopService shopService;

    @Test
    @DisplayName("Add shop with null object")
    public void test1(){

        var throwable = Assertions.assertThrows(
                MyException.class,
                () -> shopService.addShop(null)
        );

        Assertions.assertEquals("SHOP CANNOT BE NULL OR EMPTY", throwable.getExceptionMessage().getMessage(), "TEST 1 FAILED");
    }

    @Test
    @DisplayName("Add shop with wrong name")
    public void test2(){

        var shopDto = ShopDto.builder().name("lolek").build();

        var throwable = Assertions.assertThrows(
                MyException.class,
                () -> shopService.addShop(shopDto)
        );

        Assertions.assertEquals("INVALID NAME", throwable.getExceptionMessage().getMessage(), "TEST 2 FAILED");

    }
}
