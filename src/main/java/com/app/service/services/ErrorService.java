package com.app.service.services;

import com.app.dto.ErrorDto;
import com.app.exceptions.Error;
import com.app.repository.ErrorRepository;
import com.app.repository.impl.ErrorRepositoryImpl;
import com.app.service.mapper.Mappers;

import java.time.LocalDateTime;

public class ErrorService {

    private final ErrorRepository errorRepository = new ErrorRepositoryImpl();

    public void addError(String message){

        ErrorDto errorDto = ErrorDto.builder()
                .message(message)
                .date(LocalDateTime.now())
                .build();

        Error error = Mappers.fromErrorDtoToError(errorDto);
        errorRepository.addOrUpdate(error);
    }
}
