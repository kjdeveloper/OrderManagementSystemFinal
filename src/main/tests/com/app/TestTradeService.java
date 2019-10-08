package com.app;

import com.app.dto.TradeDto;
import com.app.exceptions.MyException;
import com.app.service.services.TradeService;
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
public class TestTradeService {

    @InjectMocks
    private TradeService tradeService;

    @Test
    @DisplayName("Add trade with null object")
    public void test1() {

        var throwable = Assertions.assertThrows(
                MyException.class,
                () -> tradeService.addTrade(null)
        );

        Assertions.assertEquals("TRADE IS NULL", throwable.getExceptionMessage().getMessage(), "TEST 1 FAILED");
    }

    @Test
    @DisplayName("Add trade with wrong name")
    public void test2(){

            var tradeDto = TradeDto.builder().name("trade").build();

            var throwable = Assertions.assertThrows(
                    MyException.class,
                    () -> tradeService.addTrade(tradeDto)
            );

            Assertions.assertEquals("TRADE NAME IS NOT VALID", throwable.getExceptionMessage().getMessage(), "TEST 2 FAILED");
    }
}
