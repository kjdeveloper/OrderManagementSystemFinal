package com.app;

import com.app.exceptions.Error;
import com.app.repository.ErrorRepository;
import com.app.service.services.ErrorService;
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

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TestErrorService {

    @Mock
    private ErrorRepository errorRepository;

    @InjectMocks
    private ErrorService errorService;

    @Test
    @DisplayName("Get the most error caused table")
    public void test1() {
        Error error1 = new Error(1L, Timestamp.valueOf(LocalDateTime.now()), "PRODUCT;INVALID PRODUCT");
        Error error2 = new Error(2L, Timestamp.valueOf(LocalDateTime.now()), "CUSTOMER;INVALID CUSTOMER");
        Error error3 = new Error(3L, Timestamp.valueOf(LocalDateTime.now()), "PRODUCT;INVALID PRODUCT");
        Error error4 = new Error(4L, Timestamp.valueOf(LocalDateTime.now()), "CUSTOMER;INVALID CUSTOMER");
        Error error5 = new Error(5L, Timestamp.valueOf(LocalDateTime.now()), "PRODUCT;INVALID PRODUCT");
        Error error6 = new Error(6L, Timestamp.valueOf(LocalDateTime.now()), "SHOP;INVALID SHOP");

        Mockito.when(errorRepository.findAll()).thenReturn(List.of(
                error1, error2, error3, error4, error5, error6
        ));

        var commonErrorCaused = errorService.getTheMostErrorCausedTable();

        Assertions.assertEquals("PRODUCT", commonErrorCaused, "TEST 1 FAILED");
    }

    @Test
    @DisplayName("Get the most error message")
    public void test2() {
        Error error1 = new Error(1L, Timestamp.valueOf(LocalDateTime.now()), "PRODUCT;INVALID PRODUCT");
        Error error2 = new Error(2L, Timestamp.valueOf(LocalDateTime.now()), "CUSTOMER;INVALID CUSTOMER");
        Error error3 = new Error(3L, Timestamp.valueOf(LocalDateTime.now()), "PRODUCT;INVALID PRODUCT");
        Error error4 = new Error(4L, Timestamp.valueOf(LocalDateTime.now()), "CUSTOMER;INVALID CUSTOMER");
        Error error5 = new Error(5L, Timestamp.valueOf(LocalDateTime.now()), "PRODUCT;INVALID PRODUCT");
        Error error6 = new Error(6L, Timestamp.valueOf(LocalDateTime.now()), "SHOP;INVALID SHOP");

        Mockito.when(errorRepository.findAll()).thenReturn(List.of(
                error1, error2, error3, error4, error5, error6
        ));

        var commonErrorMessage = errorService.getTheMostErrorMessage();

        Assertions.assertEquals("INVALID PRODUCT", commonErrorMessage, "TEST 2 FAILED");
    }

    @Test
    @DisplayName("Get the most error date")
    public void test3() {
        Error error1 = new Error(1L, Timestamp.valueOf(LocalDateTime.of(2018, 4,3,14,20)), "PRODUCT;INVALID PRODUCT");
        Error error2 = new Error(2L, Timestamp.valueOf(LocalDateTime.of(2019, 2, 4, 15, 40)), "CUSTOMER;INVALID CUSTOMER");
        Error error3 = new Error(3L, Timestamp.valueOf(LocalDateTime.of(2019, 2, 4, 15, 40)), "PRODUCT;INVALID PRODUCT");
        Error error4 = new Error(4L, Timestamp.valueOf(LocalDateTime.of(2019, 2, 4, 15, 40)), "CUSTOMER;INVALID CUSTOMER");
        Error error5 = new Error(5L, Timestamp.valueOf(LocalDateTime.now()), "PRODUCT;INVALID PRODUCT");
        Error error6 = new Error(6L, Timestamp.valueOf(LocalDateTime.now()), "SHOP;INVALID SHOP");

        Mockito.when(errorRepository.findAll()).thenReturn(List.of(
                error1, error2, error3, error4, error5, error6
        ));

        var commonErrorDate = errorService.getTheMostErrorDate();

        Assertions.assertEquals("2019-02-04", commonErrorDate, "TEST 3 FAILED");
    }

}
