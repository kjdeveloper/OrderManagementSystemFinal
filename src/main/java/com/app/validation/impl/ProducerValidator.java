package com.app.validation.impl;

import com.app.dto.ProducerDto;
import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;

public class ProducerValidator {

    public void validateProducer(final ProducerDto producerDto){
        if (producerDto == null){
            throw new MyException(ExceptionCode.PRODUCER, "FIELDS CAN NOT BE NULL OR EMPTY");
        }
        if (!isNameValid(producerDto)){
            throw new MyException(ExceptionCode.PRODUCER, "INVALID NAME");
        }
    }

    private boolean isNameValid(ProducerDto producerDTO) {
        String MODEL_NAME_REGEX = "[A-Z ]+";
        return producerDTO.getName().matches(MODEL_NAME_REGEX);
    }
}
