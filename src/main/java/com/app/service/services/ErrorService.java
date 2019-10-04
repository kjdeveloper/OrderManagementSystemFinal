package com.app.service.services;

import com.app.dto.ErrorDto;
import com.app.exceptions.Error;
import com.app.repository.ErrorRepository;
import com.app.repository.generic.DbConnection;
import com.app.repository.impl.ErrorRepositoryImpl;
import com.app.service.mapper.Mappers;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ErrorService {

    private final ErrorRepository errorRepository;
    private final DbConnection dbConnection = DbConnection.getInstance();

    public void addError(String message) {

        var errorDto = ErrorDto.builder()
                .message(message)
                .date(LocalDateTime.now())
                .build();

        Error error = Mappers.fromErrorDtoToError(errorDto);
        errorRepository.addOrUpdate(error);
        dbConnection.close();
    }

    public String getTheMostErrorCausedTable() {
        return errorRepository.findAll()
                .stream()
                .map(Mappers::fromErrorToErrorDto)
                .collect(Collectors.groupingBy(error -> error.getMessage()
                        .split(";")[0], Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .get()
                .getKey();
    }

    public String getTheMostErrorMessage() {
        return errorRepository.findAll()
                .stream()
                .map(Mappers::fromErrorToErrorDto)
                .collect(Collectors.groupingBy(error -> error.getMessage()
                        .split(";")[1], Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .get()
                .getKey();
    }

    public String getTheMostErrorDate() {
        return errorRepository.findAll()
                .stream()
                .map(Mappers::fromErrorToErrorDto)
                .collect(Collectors.groupingBy(error -> error.getDate()
                        .toString()
                        .split("[A-Z]")[0], Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .get()
                .getKey();
    }


}
