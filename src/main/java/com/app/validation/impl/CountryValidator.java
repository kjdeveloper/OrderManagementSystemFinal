package com.app.validation.impl;

import com.app.dto.CountryDto;
import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;

import java.util.Map;


public class CountryValidator{

    public void validateCountry(final CountryDto countryDto){
        if (countryDto == null){
            throw new MyException(ExceptionCode.COUNTRY, "COUNTRY CANNOT BE NULL OR EMPTY");
        }
        if (!isNameValid(countryDto)){
            throw new MyException(ExceptionCode.COUNTRY, "INVALID COUNTRY NAME");
        }
    }

    private boolean isNameValid(final CountryDto countryDTO) {
        String MODEL_NAME_REGEX = "[A-Z ]+";
        return countryDTO.getName().matches(MODEL_NAME_REGEX);
    }


}
