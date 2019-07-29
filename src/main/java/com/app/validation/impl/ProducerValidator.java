package com.app.validation.impl;

import com.app.dto.ProducerDto;
import com.app.exceptions.MyException;
import com.app.validation.generic.AbstractValidator;

import java.util.Map;

import static com.mysql.jdbc.StringUtils.isNullOrEmpty;

public class ProducerValidator extends AbstractValidator<ProducerDto> {

    @Override
    public Map<String, String> validate(ProducerDto producerDTO) {
        if (producerDTO == null) {
            errors.put("producerDTO", "null");
        }
        if (!isNameValid(producerDTO)) {
            errors.put("producerDTO name", "Name is not valid => " + producerDTO.getName());
        }

        return errors;
    }

    public void validateProducer(final ProducerDto producerDto){
        if (isNullOrEmpty(producerDto.getName())){
            throw new MyException("FIELDS CANNOT BE NULL OR EMPTY");
        }
        if (!isNameValid(producerDto)){
            throw new MyException("INVALID NAME");
        }
    }

    private boolean isNameValid(ProducerDto producerDTO) {
        String MODEL_NAME_REGEX = "[A-Z ]+";
        return producerDTO.getName().matches(MODEL_NAME_REGEX);
    }
}
