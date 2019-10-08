package com.app;

import com.app.dto.CountryDto;
import com.app.exceptions.MyException;
import com.app.model.Country;
import com.app.service.services.CountryService;
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
public class TestCountryService {

    @InjectMocks
    private CountryService countryService;

    @Test
    @DisplayName("Add country with null object")
    public void test1(){

        var throwable = Assertions.assertThrows(
                MyException.class,
                () -> countryService.addCountry(null)
        );

        Assertions.assertEquals("COUNTRY CANNOT BE NULL OR EMPTY", throwable.getExceptionMessage().getMessage(), "TEST 1 FAILED");
    }

    @Test
    @DisplayName("Add country with incorrect name")
    public void test2(){

        var countryDto = CountryDto.builder().name("pakistan").build();

        var throwable = Assertions.assertThrows(
                MyException.class,
                () -> countryService.addCountry(countryDto)
        );

        Assertions.assertEquals("INVALID COUNTRY NAME", throwable.getExceptionMessage().getMessage(), "TEST 2 FAILED");
    }
}
