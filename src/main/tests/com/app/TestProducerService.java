package com.app;

import com.app.dto.ProducerDto;
import com.app.exceptions.MyException;
import com.app.service.services.ProducerService;
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
public class TestProducerService {

    @InjectMocks
    private ProducerService producerService;

    @Test
    @DisplayName("Add producer with wrong name")
    public void test1(){

        var producerDto = ProducerDto.builder().name("wrongName").build();

        var throwable = Assertions.assertThrows(
                MyException.class,
                () -> producerService.addProducer(producerDto)
        );

        Assertions.assertEquals("INVALID NAME", throwable.getExceptionMessage().getMessage(), "TEST 1 FAILED");
    }

    @Test
    @DisplayName("Add producer with null object")
    public void test2(){

        var throwable = Assertions.assertThrows(
                MyException.class,
                () -> producerService.addProducer(null)
        );

        Assertions.assertEquals("FIELDS CANNOT BE NULL OR EMPTY", throwable.getExceptionMessage().getMessage(), "TEST 2 FAILED");
    }
}
